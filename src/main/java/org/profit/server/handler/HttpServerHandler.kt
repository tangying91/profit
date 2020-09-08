package org.profit.server.handler

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import org.profit.app.Actions
import org.profit.server.handler.communication.RequestMessage
import org.profit.server.handler.communication.exception.InvalidActionException
import org.profit.server.handler.communication.exception.InvalidParamException
import org.slf4j.LoggerFactory

/**
 * @author TangYing
 */
@Sharable
class HttpServerHandler : SimpleChannelInboundHandler<FullHttpRequest>() {
    private val staticFileHandler = HttpStaticFileHandler()
    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        if (request.method !== HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }
        val decoder = QueryStringDecoder(request.uri)
        val parameters = decoder.parameters()
        val path = decoder.path()
        if (!path.endsWith(actionSuffix)) {
            staticFileHandler.handleStaticFile(ctx, request)
            return
        }
        // Action request
        val actionUri = getActionUri(path)
        val actionName = actionUri.replace(actionSuffix.toRegex(), "")
        if (actionName.contains("_")) {
            try {
                val action = Actions.INSTANCE.interpretCommand(actionName)
                if (action == null) {
                    sendError(ctx, HttpResponseStatus.FORBIDDEN)
                    return
                }
                // Execute action
                val requestMessage = RequestMessage(parameters)
                action.execute(ctx, requestMessage)
            } catch (ipe: InvalidParamException) {
                LOG.error("InvalidParamException. {}", ipe.message)
                sendError(ctx, HttpResponseStatus.FORBIDDEN)
            } catch (iae: InvalidActionException) {
                LOG.error("InvalidActionException. {}", iae.message)
                sendError(ctx, HttpResponseStatus.FORBIDDEN)
            }
            return
        }
        // Sya forbidden
        sendError(ctx, HttpResponseStatus.FORBIDDEN)
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

    /**
     * Get action URL
     *
     * @param uri
     * @return
     */
    protected fun getActionUri(uri: String): String {
        val baseUri = getBaseUri(uri)
        return uri.substring(uri.indexOf(baseUri) + baseUri.length)
    }

    /**
     * Get base URL
     *
     * @param uri
     * @return
     */
    protected fun getBaseUri(uri: String): String {
        val idx = uri.indexOf("/", 1)
        return if (idx == -1) {
            "/"
        } else {
            uri.substring(0, idx)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(HttpServerHandler::class.java)
        private const val actionSuffix = ".action"
        /**
         * Send error status
         *
         * @param ctx
         * @param status
         */
        private fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
            val response: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: $status\r\n", CharsetUtil.UTF_8))
            response.headers()[HttpHeaders.Names.CONTENT_TYPE] = "text/plain; charset=UTF-8"
            // Close the connection as soon as the error message is sent.
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        }
    }
}