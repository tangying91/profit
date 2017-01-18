package org.profit.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.profit.server.handler.communication.RequestMessage;
import org.profit.server.handler.communication.exception.InvalidActionException;
import org.profit.server.handler.communication.exception.InvalidParamException;
import org.profit.server.handler.action.Action;
import org.profit.app.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author TangYing
 */
@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServerHandler.class);

    private HttpStaticFileHandler staticFileHandler = new HttpStaticFileHandler();

    private final static String actionSuffix = ".action";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (request.getMethod() != GET) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> parameters = decoder.parameters();
        String path = decoder.path();
        if (!path.endsWith(actionSuffix)) {
            staticFileHandler.handleStaticFile(ctx, request);
            return;
        }

        // Action request
        String actionUri = getActionUri(path);
        String actionName = actionUri.replaceAll(actionSuffix, "");
        if (actionName.contains("_")) {
            try {
                Action action = Actions.INSTANCE.interpretCommand(actionName);
                if (action == null) {
                    sendError(ctx, FORBIDDEN);
                    return;
                }

                // Execute action
                RequestMessage requestMessage = new RequestMessage(parameters);
                action.execute(ctx, requestMessage);
            } catch (InvalidParamException ipe) {
                LOG.error("InvalidParamException. {}", ipe.getMessage());
                sendError(ctx, FORBIDDEN);
            } catch (InvalidActionException iae) {
                LOG.error("InvalidActionException. {}", iae.getMessage());
                sendError(ctx, FORBIDDEN);
            }
            return;
        }

        // Sya forbidden
        sendError(ctx, FORBIDDEN);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * Send error status
     *
     * @param ctx
     * @param status
     */
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Get action URL
     *
     * @param uri
     * @return
     */
    protected String getActionUri(String uri) {
        String baseUri = getBaseUri(uri);
        return uri.substring(uri.indexOf(baseUri) + baseUri.length());
    }

    /**
     * Get base URL
     *
     * @param uri
     * @return
     */
    protected String getBaseUri(String uri) {
        int idx = uri.indexOf("/", 1);
        if (idx == -1) {
            return "/";
        } else {
            return uri.substring(0, idx);
        }
    }
}
