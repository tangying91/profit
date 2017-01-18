package org.profit.server.handler.action;

import io.netty.channel.ChannelHandlerContext;
import org.profit.server.handler.communication.RequestMessage;
import org.profit.server.handler.communication.exception.InvalidParamException;

/**
 * Created by TangYing
 */
public interface Action {

    /**
     * Interface of Action
     */
    public void execute(ChannelHandlerContext ctx, RequestMessage request) throws InvalidParamException;
}
