package org.profit.app.action;

import io.netty.channel.ChannelHandlerContext;
import org.profit.server.handler.communication.RequestMessage;
import org.profit.server.handler.communication.exception.InvalidParamException;
import org.profit.app.realm.StockHall;
import org.profit.app.realm.StockRobot;
import org.profit.persist.domain.Stock;
import org.profit.server.handler.action.AbstractAction;
import org.profit.util.DateUtils;

public class STOCK_POOL_GENERATE extends AbstractAction {

    @Override
    public void execute(ChannelHandlerContext ctx, RequestMessage request) throws InvalidParamException {
        request.requireParameters("code");

        String code = request.getStringParameter("code");

        Stock stock = StockHall.getStock(code);
        if (stock == null ) {
            sendJsonResponse(ctx, "Stock not existed.");
        }

        String lastDay = DateUtils.formatDate(System.currentTimeMillis());
        StockRobot.run(stock, lastDay);
        sendJsonResponse(ctx, "Stock analytic completed.");
    }
}
