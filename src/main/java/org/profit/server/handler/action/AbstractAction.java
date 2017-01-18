package org.profit.server.handler.action;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.profit.server.handler.communication.RequestMessage;
import org.profit.server.handler.communication.exception.InvalidParamException;
import org.profit.config.PersistContext;
import org.profit.persist.mapper.stock.PoolMapper;
import org.profit.persist.mapper.sys.MenuMapper;
import org.profit.persist.mapper.sys.UserMapper;
import org.profit.config.AppContext;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public abstract class AbstractAction implements Action {

    /**
     * Execute
     *
     * @param ctx
     * @param request
     * @return
     */
    public abstract void execute(ChannelHandlerContext ctx, RequestMessage request) throws InvalidParamException;

    /**
     * Send json data
     *
     * @param ctx
     * @param json
     */
    protected void sendJsonResponse(ChannelHandlerContext ctx, String json) {
        // Create http response
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.content().writeBytes((Unpooled.copiedBuffer(json, CharsetUtil.UTF_8)));

        // Close the connection as soon as the error message is sent
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    // Mapper
    protected static MenuMapper menuMapper = (MenuMapper) AppContext.getBean(PersistContext.MENU_MAPPER);
    protected static UserMapper userMapper = (UserMapper) AppContext.getBean(PersistContext.USER_MAPPER);
    protected static PoolMapper poolMapper = (PoolMapper) AppContext.getBean(PersistContext.STOCK_POOL_MAPPER);
}
