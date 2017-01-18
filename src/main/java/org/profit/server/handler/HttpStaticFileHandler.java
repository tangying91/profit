package org.profit.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpStaticFileHandler {

	private static final Logger LOG = LoggerFactory.getLogger(HttpStaticFileHandler.class);

	private static final MimetypesFileTypeMap MIMETYPES_FILE_TYPE_MAP;

	public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	public static final int HTTP_CACHE_SECONDS = 60;
	public static final String WEB_ROOT = "webroot";

	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

	static {
		MIMETYPES_FILE_TYPE_MAP = new MimetypesFileTypeMap();
		MIMETYPES_FILE_TYPE_MAP.addMimeTypes("application/xml xml");
		MIMETYPES_FILE_TYPE_MAP.addMimeTypes("text/html html");
		MIMETYPES_FILE_TYPE_MAP.addMimeTypes("text/css css");
		MIMETYPES_FILE_TYPE_MAP.addMimeTypes("application/javascript js");
		MIMETYPES_FILE_TYPE_MAP.addMimeTypes("application/json json");
	}

	public void handleStaticFile(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, BAD_REQUEST);
			return;
		}

		if (request.getMethod() != GET) {
			sendError(ctx, METHOD_NOT_ALLOWED);
			return;
		}

		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		String uri = decoder.path();
		if (uri.equals("/")) {
			uri = "/index.html";
		} else if (uri.endsWith("en")) {
			uri = "/index-en.html";
		}

//        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
//        final String uri = decoder.path();
//		final String path = sanitizeUri(uri);
//		if (path == null) {
//			sendError(ctx, FORBIDDEN);
//			return;
//		}

		final String path = sanitizeUri(uri);
		if (path == null) {
			sendError(ctx, FORBIDDEN);
			return;
		}

		LOG.debug("Request file path: {}", path);

		File file = new File(path);
		if (file.isHidden() || !file.exists()) {
			sendError(ctx, NOT_FOUND);
			return;
		}
		if (!file.isFile()) {
			sendError(ctx, FORBIDDEN);
			return;
		}

		// Cache Validation
		String ifModifiedSince = request.headers().get(IF_MODIFIED_SINCE);
		if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
			Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);

			// Only compare up to the second because the datetime format we send to the client
			// does not have milliseconds
			long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
			long fileLastModifiedSeconds = file.lastModified() / 1000;
			if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
				sendNotModified(ctx);
				return;
			}
		}

		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException fnfe) {
			sendError(ctx, NOT_FOUND);
			return;
		}

		long fileLength = raf.length();
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		setContentLength(response, fileLength);
		setContentTypeHeader(response, file);
		setDateAndCacheHeaders(response, file);
		if (isKeepAlive(request)) {
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}

		// Write the initial line and the header.
		ctx.write(response);

		ChannelFuture sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
		sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
				if (total < 0) { // total unknown
					LOG.debug("Transfer progress: {}", progress);
				} else {
					LOG.debug("Transfer progress: {}", progress + " / " + total);
				}
			}

			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				LOG.debug("Transfer complete.");
			}
		});

		// Write the end marker
		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

		// Decide whether to close the connection or not.
		if (!isKeepAlive(request)) {
			// Close the connection when the whole content is written out.
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static String sanitizeUri(String uri) {
		// Decode the path.
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error();
			}
		}

		if (!uri.startsWith("/")) {
			return null;
		}

		// Convert file separators.
		uri = uri.replace('/', File.separatorChar);

		// Simplistic dumb security check.
		// You will have to do something serious in the production environment.
		if (uri.contains(File.separator + '.') ||
				uri.contains('.' + File.separator) ||
				uri.startsWith(".") || uri.endsWith(".") ||
				INSECURE_URI.matcher(uri).matches()) {
			return null;
		}

		// Convert to absolute path.
        String path = System.getProperty("user.dir") + File.separator + WEB_ROOT + File.separator + uri;
		return path;
	}

	private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
		response.headers().set(LOCATION, newUri);

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * When file timestamp is the same as what the browser is sending up, send a "304 Not Modified"
	 *
	 * @param ctx Context
	 */
	private static void sendNotModified(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
		setDateHeader(response);

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Sets the Date header for the HTTP response
	 *
	 * @param response HTTP response
	 */
	private static void setDateHeader(FullHttpResponse response) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		Calendar time = new GregorianCalendar();
		response.headers().set(DATE, dateFormatter.format(time.getTime()));
	}

	/**
	 * Sets the Date and Cache headers for the HTTP Response
	 *
	 * @param response    HTTP response
	 * @param fileToCache file to extract content type
	 */
	private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		// Date header
		Calendar time = new GregorianCalendar();
		response.headers().set(DATE, dateFormatter.format(time.getTime()));

		// Add cache headers
		time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
		response.headers().set(EXPIRES, dateFormatter.format(time.getTime()));
		response.headers().set(CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
		response.headers().set(LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
	}

	/**
	 * Sets the content type header for the HTTP Response
	 *
	 * @param response HTTP response
	 * @param file     file to extract content type
	 */
	private static void setContentTypeHeader(HttpResponse response, File file) {
		response.headers().set(CONTENT_TYPE, MIMETYPES_FILE_TYPE_MAP.getContentType(file.getPath()));
	}
}
