package org.profit.server.handler

import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.activation.MimetypesFileTypeMap

class HttpStaticFileHandler {
    companion object {
        private val LOG = LoggerFactory.getLogger(HttpStaticFileHandler::class.java)

        val MIMETYPES_FILE_TYPE_MAP: MimetypesFileTypeMap = MimetypesFileTypeMap()

        const val HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz"
        const val HTTP_DATE_GMT_TIMEZONE = "GMT"
        const val HTTP_CACHE_SECONDS = 60
        const val WEB_ROOT = "webroot"
        private val INSECURE_URI = Pattern.compile(".*[<>&\"].*")
        private fun sanitizeUri(uri: String): String? { // Decode the path.
            var uri = uri
            uri = try {
                URLDecoder.decode(uri, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                try {
                    URLDecoder.decode(uri, "ISO-8859-1")
                } catch (e1: UnsupportedEncodingException) {
                    throw Error()
                }
            }
            if (!uri.startsWith("/")) {
                return null
            }
            // Convert file separators.
            uri = uri.replace('/', File.separatorChar)
            // Simplistic dumb security check.
// You will have to do something serious in the production environment.
            return if (uri.contains(File.separator + '.') ||
                    uri.contains('.'.toString() + File.separator) ||
                    uri.startsWith(".") || uri.endsWith(".") ||
                    INSECURE_URI.matcher(uri).matches()) {
                null
            } else System.getProperty("user.dir") + File.separator + WEB_ROOT + File.separator + uri
            // Convert to absolute path.
        }

        private fun sendRedirect(ctx: ChannelHandlerContext, newUri: String) {
            val response: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND)
            response.headers()[HttpHeaders.Names.LOCATION] = newUri
            // Close the connection as soon as the error message is sent.
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        }

        private fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
            val response: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: $status\r\n", CharsetUtil.UTF_8))
            response.headers()[HttpHeaders.Names.CONTENT_TYPE] = "text/plain; charset=UTF-8"
            // Close the connection as soon as the error message is sent.
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        }

        /**
         * When file timestamp is the same as what the browser is sending up, send a "304 Not Modified"
         *
         * @param ctx Context
         */
        private fun sendNotModified(ctx: ChannelHandlerContext) {
            val response: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_MODIFIED)
            setDateHeader(response)
            // Close the connection as soon as the error message is sent.
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        }

        /**
         * Sets the Date header for the HTTP response
         *
         * @param response HTTP response
         */
        private fun setDateHeader(response: FullHttpResponse) {
            val dateFormatter = SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US)
            dateFormatter.timeZone = TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE)
            val time: Calendar = GregorianCalendar()
            response.headers()[HttpHeaders.Names.DATE] = dateFormatter.format(time.time)
        }

        /**
         * Sets the Date and Cache headers for the HTTP Response
         *
         * @param response    HTTP response
         * @param fileToCache file to extract content type
         */
        private fun setDateAndCacheHeaders(response: HttpResponse, fileToCache: File) {
            val dateFormatter = SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US)
            dateFormatter.timeZone = TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE)
            // Date header
            val time: Calendar = GregorianCalendar()
            response.headers()[HttpHeaders.Names.DATE] = dateFormatter.format(time.time)
            // Add cache headers
            time.add(Calendar.SECOND, HTTP_CACHE_SECONDS)
            response.headers()[HttpHeaders.Names.EXPIRES] = dateFormatter.format(time.time)
            response.headers()[HttpHeaders.Names.CACHE_CONTROL] = "private, max-age=$HTTP_CACHE_SECONDS"
            response.headers()[HttpHeaders.Names.LAST_MODIFIED] = dateFormatter.format(Date(fileToCache.lastModified()))
        }

        /**
         * Sets the content type header for the HTTP Response
         *
         * @param response HTTP response
         * @param file     file to extract content type
         */
        private fun setContentTypeHeader(response: HttpResponse, file: File) {
            response.headers()[HttpHeaders.Names.CONTENT_TYPE] = MIMETYPES_FILE_TYPE_MAP!!.getContentType(file.path)
        }

        init {
            MIMETYPES_FILE_TYPE_MAP.addMimeTypes("application/xml xml")
            MIMETYPES_FILE_TYPE_MAP.addMimeTypes("text/html html")
            MIMETYPES_FILE_TYPE_MAP.addMimeTypes("text/css css")
            MIMETYPES_FILE_TYPE_MAP.addMimeTypes("application/javascript js")
            MIMETYPES_FILE_TYPE_MAP.addMimeTypes("application/json json")
        }
    }

    @Throws(Exception::class)
    fun handleStaticFile(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        if (!request.decoderResult.isSuccess) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST)
            return
        }
        if (request.method !== HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED)
            return
        }
        val decoder = QueryStringDecoder(request.uri)
        var uri = decoder.path()
        if (uri == "/") {
            uri = "/index.html"
        } else if (uri.endsWith("en")) {
            uri = "/index-en.html"
        }
        //        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
//        final String uri = decoder.path();
//		final String path = sanitizeUri(uri);
//		if (path == null) {
//			sendError(ctx, FORBIDDEN);
//			return;
//		}
        val path = sanitizeUri(uri)
        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }
        LOG.debug("Request file path: {}", path)
        val file = File(path)
        if (file.isHidden || !file.exists()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        if (!file.isFile) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }
        // Cache Validation
        val ifModifiedSince = request.headers()[HttpHeaders.Names.IF_MODIFIED_SINCE]
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
            val dateFormatter = SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US)
            val ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince)
            // Only compare up to the second because the datetime format we send to the client
// does not have milliseconds
            val ifModifiedSinceDateSeconds = ifModifiedSinceDate.time / 1000
            val fileLastModifiedSeconds = file.lastModified() / 1000
            if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
                sendNotModified(ctx)
                return
            }
        }
        val raf: RandomAccessFile
        raf = try {
            RandomAccessFile(file, "r")
        } catch (fnfe: FileNotFoundException) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        val fileLength = raf.length()
        val response: HttpResponse = DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        HttpHeaders.setContentLength(response, fileLength)
        setContentTypeHeader(response, file)
        setDateAndCacheHeaders(response, file)
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers()[HttpHeaders.Names.CONNECTION] = HttpHeaders.Values.KEEP_ALIVE
        }
        // Write the initial line and the header.
        ctx.write(response)
        val sendFileFuture = ctx.write(DefaultFileRegion(raf.channel, 0, fileLength), ctx.newProgressivePromise())
        sendFileFuture.addListener(object : ChannelProgressiveFutureListener {
            override fun operationProgressed(future: ChannelProgressiveFuture, progress: Long, total: Long) {
                if (total < 0) { // total unknown
                    LOG.debug("Transfer progress: {}", progress)
                } else {
                    LOG.debug("Transfer progress: {}", "$progress / $total")
                }
            }

            @Throws(Exception::class)
            override fun operationComplete(future: ChannelProgressiveFuture) {
                LOG.debug("Transfer complete.")
            }
        })
        // Write the end marker
        val lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
        // Decide whether to close the connection or not.
        if (!HttpHeaders.isKeepAlive(request)) { // Close the connection when the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE)
        }
    }
}