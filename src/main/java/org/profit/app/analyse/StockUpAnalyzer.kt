package org.profit.app.analyse

import org.profit.app.StockHall
import org.profit.app.pojo.StockHistory
import org.profit.util.DateUtils
import org.profit.util.FileUtils
import org.profit.util.StockUtils
import java.lang.Exception

/**
 * 【积】周期内,连续上涨天数较多
 */
class StockUpAnalyzer(code: String, private val statCount: Int, private val upPercent: Double) : StockAnalyzer(code) {

    /**
     * 1.周期内总天数
     * 2.周期内连续上涨最大天数
     * 3.周内总上涨天数
     */
    override fun analyse(results: MutableList<String>) {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 如果没有数据
        if (list.size < statCount) {
            return
        }

        val totalDay = list.size
        val riseDays = list.count { it.close > it.open }
        val risePercent = (list[0].close - list[statCount - 1].open).div(list[statCount - 1].open) * 100
        if (riseDays > totalDay * upPercent) {
            val content = "$code${StockHall.stockName(code)} 一共$totalDay 天，累计一共上涨$riseDays 天，区间涨幅${StockUtils.twoDigits(risePercent)}% ,快速查看: http://stockpage.10jqka.com.cn/$code/"
            FileUtils.writeStat(content)
        }
    }
}