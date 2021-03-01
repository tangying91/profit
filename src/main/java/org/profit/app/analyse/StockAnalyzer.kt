package org.profit.app.analyse

import org.profit.app.pojo.StockHistory
import org.profit.util.DateUtils
import org.profit.util.FileUtils
import java.lang.Exception

/**
 * 股票数据分析器
 */
abstract class StockAnalyzer(val code: String) {

    abstract fun analyse(results: MutableList<String>)

    /**
     * 获取历史数据
     */
    fun readHistories(statCount: Int): List<StockHistory> {
        // 获取数据，后期可以限制天数
        val histories = mutableListOf<StockHistory>()
        FileUtils.readHistory(code).forEach {
            try {
                // 日期 开盘 最高 最低 收盘 成交量 成交金额 涨跌金额 涨跌百分比 缩 高低差% SZ深证 SZ%
                val array = it.split(" ")

                val history = StockHistory()
                history.date = array[0]
                history.dateTime = DateUtils.parseTime(array[0], "MM/dd/yyyy")
                history.open = array[1].toDouble()
                history.high = array[2].toDouble()
                history.low = array[3].toDouble()
                history.close = array[4].toDouble()
                history.volume = array[5].replace(",", "").toLong()
                history.turnover = array[6].replace(",", "").toLong()
                history.stockPercent = array[8].toDouble()
                history.stockIndexPercent = array[14].replace("/", "0").toDouble()
                histories.add(history)
            } catch (e: Exception) {
                // Do nothing
            }
        }

        // 截取周期
        val end = kotlin.math.min(statCount, histories.size)
        return histories.sortedByDescending { it.dateTime }.subList(0, end)
    }
}