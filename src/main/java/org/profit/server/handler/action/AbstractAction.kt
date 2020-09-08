package org.profit.server.handler.action

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import org.profit.server.handler.communication.RequestMessage
import org.profit.server.handler.communication.exception.InvalidParamException

abstract class AbstractAction : Action {
    /**
     * Execute
     *
     * @param ctx
     * @param request
     * @return
     */
    @Throws(InvalidParamException::class)
    abstract override fun execute(ctx: ChannelHandlerContext?, request: RequestMessage)

    /**
     * Send json data
     *
     * @param ctx
     * @param json
     */
    protected fun sendJsonResponse(ctx: ChannelHandlerContext, json: String?) { // Create http response
        val response: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        response.headers()[HttpHeaders.Names.CONTENT_TYPE] = "text/plain; charset=UTF-8"
        response.content().writeBytes(Unpooled.copiedBuffer(json, CharsetUtil.UTF_8))
        // Close the connection as soon as the error message is sent
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }
}