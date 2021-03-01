package org.profit.app.analyse

import org.profit.app.StockHall
import org.profit.util.FileUtils
import org.profit.util.StockUtils
import kotlin.math.abs

/**
 * 股价历史回顾
 * 在一个周期内，股价波动在一定范围内
 */
class StockHistoryAnalyzer(code: String, private val statCount: Int, private val stockPercent: Double) : StockAnalyzer(code) {

    override fun analyse(results: MutableList<String>) {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)
        if (list.size != statCount) {
            return
        }

        val totalDay = list.size
        val percent = (list[0].close - list[statCount - 1].open).div(list[statCount - 1].open)
        if (abs(percent) in -stockPercent..stockPercent) {
            val content = "$code${StockHall.stockName(code)} 一共$totalDay 天，区间涨幅${StockUtils.twoDigits(percent * 100)}% ,快速查看: http://stockpage.10jqka.com.cn/$code/"
            FileUtils.writeStat(content)
        }
    }
}