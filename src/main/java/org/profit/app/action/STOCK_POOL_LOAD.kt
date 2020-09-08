package org.profit.app.action

import com.google.gson.Gson
import io.netty.channel.ChannelHandlerContext
import org.profit.persist.domain.StockStat
import org.profit.server.handler.action.AbstractAction
import org.profit.server.handler.communication.RequestMessage
import org.profit.server.handler.communication.data.ApiResponse
import org.profit.server.handler.communication.exception.InvalidParamException
import java.util.*

class STOCK_POOL_LOAD : AbstractAction() {

    @Throws(InvalidParamException::class)
    override fun execute(ctx: ChannelHandlerContext?, request: RequestMessage) {
        request.requireParameters("type", "day", "riseDay", "volumeRate", "gainsRange", "fundRange")
        val start = request.getIntegerParameter("start")
        val limit = request.getIntegerParameter("limit")
        val type = request.getIntegerParameter("type")
        val day = request.getStringParameter("day")
        val riseDay = request.getIntegerParameter("riseDay")
        val volumeRate = request.getDoubleParameter("volumeRate")

        // 涨幅范围和资金流范围
//        val gainsRange = request.getStringParameter("gainsRange")
//        val fundRange = request.getStringParameter("fundRange")
//        val funds = fundRange!!.split(",").toTypedArray()
//        val gains = gainsRange!!.split(",").toTypedArray()
//        if (funds.size >= 4 && gains.size >= 2) {
//            val min = funds[0].toInt()
//            val mid = funds[1].toInt()
//            val big = funds[2].toInt()
//            val max = funds[3].toInt()
//            val gainsMin = gains[0].toDouble() / 100
//            val gainsMax = gains[1].toDouble() / 100
//            // 获取数据
//            val list1: List<StockStat> = AbstractAction.Companion.poolMapper!!.selectPools(day, type, riseDay, volumeRate, gainsMin, gainsMax, min.toDouble(), mid.toDouble(), big.toDouble(), max.toDouble(), start, limit)
//            // 股票名称处理
//            val list: MutableList<StockNode> = ArrayList()
//            for (pool in list1) {
//                list.add(StockNode(pool))
//            }
//            val totalCount = list.size
//            sendJsonResponse(ctx!!, Gson().toJson(ApiResponse(true, totalCount.toLong(), list)))
//        } else {
//            sendJsonResponse(ctx!!, Gson().toJson(ApiResponse(true, 0, ArrayList<StockNode>())))
//        }
    }
}