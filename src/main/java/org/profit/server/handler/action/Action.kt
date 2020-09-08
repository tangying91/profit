package org.profit.server.handler.action

import io.netty.channel.ChannelHandlerContext
import org.profit.server.handler.communication.RequestMessage
import org.profit.server.handler.communication.exception.InvalidParamException

/**
 * Created by TangYing
 */
interface Action {
    /**
     * Interface of Action
     */
    @Throws(InvalidParamException::class)
    fun execute(ctx: ChannelHandlerContext?, request: RequestMessage)
}