package org.profit.app.analyse

import org.profit.app.StockHall
import org.profit.util.StockUtils

/**
 * 【突】周期末,股价成交量大幅拉升
 * 周期内成交量是否有异动
 */
class StockVolumeAnalyzer(code: String, private val statCount: Int, private val statRate: Double) : StockAnalyzer(code) {

    /**
     * 1.周期内平均成交量（去除最低和最高）
     * 2.最高成交量和平均成交量的关系
     * 3.周内成交量上涨天数
     */
    override fun analyse(results: MutableList<String>) {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 最近三天成交量出现平均成交量N倍的情况
        if (list.size <= 3 || list.size != statCount) {
            return
        }

        val totalDay = list.size
        val percent = (list[0].close - list[0].open).div(list[0].open)
        val avgVolume = (list.filter { it != list[0] }.maxBy { it.volume  }?.volume ?: 0L) / 10000
        val lastVolume = list[0].volume / 10000

        val rate = lastVolume.div(avgVolume.toDouble())
        if (rate >= statRate && percent >= 0.03) {
            val content = "$code${StockHall.stockName(code)} 一共$totalDay 天，最近成交量出现异动，最新涨幅${StockUtils.twoDigits(percent * 100)}% ,快速查看: http://stockpage.10jqka.com.cn/$code/"
            results.add(content)
        }
    }
}