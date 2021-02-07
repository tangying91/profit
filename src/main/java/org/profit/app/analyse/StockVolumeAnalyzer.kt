package org.profit.app.analyse

import org.profit.app.StockHall
import org.profit.util.StockUtils

/**
 * 【突】周期末,股价成交量大幅拉升
 * 周期内成交量是否有异动
 */
class StockVolumeAnalyzer(code: String, private val statCount: Int, private val statRate: Int) : StockAnalyzer(code) {

    /**
     * 1.周期内平均成交量（去除最低和最高）
     * 2.最高成交量和平均成交量的关系
     * 3.周内成交量上涨天数
     */
    override fun analyse() {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 最近三天成交量出现平均成交量N倍的情况
        if (list.size <= 3 || list.size != statCount) {
            return
        }

        val totalDay = list.size
        val avgVolume = (list.map { it.volume }.sum() - (list.maxBy { it.volume }?.volume ?: 0)).div(totalDay - 1)
        val risePercent = (list[0].close - list[statCount - 1].open).div(list[statCount - 1].open) * 100

        var satisfy = false
        for (i in 0 until 1) {
            satisfy = list[i].volume.div(avgVolume) >= statRate || satisfy
        }

        if (satisfy && risePercent > -0.03) {
            val content = "$code${StockHall.stockName(code)} 一共$totalDay 天，最近成交量出现异动，区间涨幅${StockUtils.twoDigits(risePercent)}% ,快速查看: http://stockpage.10jqka.com.cn/$code/"
            println(content)
        }
    }
}