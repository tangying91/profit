package org.profit.app.action;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import org.profit.server.handler.communication.RequestMessage;
import org.profit.server.handler.communication.exception.InvalidParamException;
import org.profit.persist.domain.stock.Pool;
import org.profit.server.handler.communication.data.ApiResponse;
import org.profit.app.vo.StockNode;
import org.profit.server.handler.action.AbstractAction;

import java.util.ArrayList;
import java.util.List;

public class STOCK_POOL_LOAD extends AbstractAction {

    @Override
    public void execute(ChannelHandlerContext ctx, RequestMessage request) throws InvalidParamException {
        request.requireParameters("type", "day", "riseDay", "volumeRate", "gainsRange", "fundRange");

        int start = request.getIntegerParameter("start");
        int limit = request.getIntegerParameter("limit");
        int type = request.getIntegerParameter("type");
        String day = request.getStringParameter("day");
        int riseDay = request.getIntegerParameter("riseDay");
        double volumeRate = request.getDoubleParameter("volumeRate");

        // 涨幅范围和资金流范围
        String gainsRange = request.getStringParameter("gainsRange");
        String fundRange = request.getStringParameter("fundRange");
        String[] funds = fundRange.split(",");
        String[] gains = gainsRange.split(",");

        if (funds.length >= 4 && gains.length >=2) {
            int min = Integer.parseInt(funds[0]);
            int mid = Integer.parseInt(funds[1]);
            int big = Integer.parseInt(funds[2]);
            int max = Integer.parseInt(funds[3]);

            double gainsMin = Double.parseDouble(gains[0]) / 100;
            double gainsMax = Double.parseDouble(gains[1]) / 100;

            // 获取数据
            List<Pool> list1= poolMapper.selectPools(day, type, riseDay, volumeRate, gainsMin, gainsMax, min, mid, big, max, start, limit);

            // 股票名称处理
            List<StockNode> list = new ArrayList<StockNode>();
            for (Pool pool : list1) {
                list.add(new StockNode(pool));
            }

            int totalCount = list.size();
            sendJsonResponse(ctx, new Gson().toJson(new ApiResponse<StockNode>(true, totalCount, list)));

        } else {
            sendJsonResponse(ctx, new Gson().toJson(new ApiResponse<StockNode>(true, 0, new ArrayList<StockNode>())));
        }
    }
}
