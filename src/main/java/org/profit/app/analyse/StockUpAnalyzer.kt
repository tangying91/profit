package org.profit.app.analyse

import org.profit.app.pojo.StockHistory
import org.profit.util.DateUtils
import org.profit.util.FileUtils
import java.lang.Exception

/**
 * 【积】周期内,连续上涨天数较多
 */
class StockUpAnalyzer(code: String): StockAnalyzer(code) {

    /**
     * 统计样本数
     */
    val statCount = 10

    /**
     * 1.周期内总天数
     * 1.周期内连续上涨最大天数
     * 2.周内总上涨天数
     * 3.跑赢大盘天数
     */
    override fun analyse() {
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

        // 如果没有数据
        if (histories.isEmpty()) {
            println("$code 数据为空")
            return
        }

        // 截取周期，假设30天
        val end = kotlin.math.min(statCount, histories.size)
        val list = histories.sortedByDescending { it.dateTime }.subList(0, end)

        val totalDay = list.size
        val serialRiseDays = serialDaysOfRise(list)
        val riseDays = list.count { it.stockPercent > 0.0 }
        val winIndexDays = list.count { it.stockPercent > it.stockIndexPercent }

        println("$code 分析结果: 一共$totalDay 天，最大连续上涨$serialRiseDays 天，累计一共上涨$riseDays 天 ")
    }

    private fun serialDaysOfRise(list: List<StockHistory>): Int {
        var maxCount = 0
        var count = 0
        for (i in 1 until list.size) {
            if (list[i].close > list[i - 1].close) {
                count++
            } else {
                count = 0
            }
            maxCount = kotlin.math.max(maxCount, count)
        }
        return maxCount
    }
}