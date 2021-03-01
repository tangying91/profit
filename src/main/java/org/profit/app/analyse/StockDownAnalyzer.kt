package org.profit.app.analyse

import org.profit.app.StockHall
import org.profit.app.pojo.StockHistory
import org.profit.util.DateUtils
import org.profit.util.FileUtils
import org.profit.util.StockUtils
import java.lang.Exception

/**
 * 【积】周期内,连续下跌天数较多
 */
class StockDownAnalyzer(code: String, private val statCount: Int, private val downPercent: Double) : StockAnalyzer(code) {

    /**
     * 1.周期内总天数
     * 2.周期内连续下跌最大天数
     * 3.周内总下跌天数
     */
    override fun analyse(results: MutableList<String>) {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 如果没有数据
        if (list.size < statCount) {
            return
        }

        val totalDay = list.size
        val downDays = list.count { it.close < it.open }
        val percent = (list[0].close - list[statCount - 1].open).div(list[statCount - 1].open) * 100
        if (downDays > totalDay * downPercent) {
            val content = "$code${StockHall.stockName(code)} 一共$totalDay 天，累计一共下跌$downDays 天，区间涨幅${StockUtils.twoDigits(percent)}% ,快速查看: http://stockpage.10jqka.com.cn/$code/"
            FileUtils.writeStat(content)
        }
    }
}