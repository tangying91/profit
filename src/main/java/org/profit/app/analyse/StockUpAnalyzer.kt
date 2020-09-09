package org.profit.app.analyse

import org.profit.app.StockHall
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
    private val statCount = 10

    /**
     * 1.周期内总天数
     * 2.周期内连续上涨最大天数
     * 3.周内总上涨天数
     */
    override fun analyse() {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 如果没有数据
        if (list.isEmpty()) {
//            println("$code 数据为空")
            return
        }

        val totalDay = list.size
        val serialRiseDays = serialDaysOfRise(list)
        val riseDays = list.count { it.stockPercent > 0.0 }
        val result = if (riseDays.div(totalDay.toDouble()) >= 0.8) {
            "强势上涨中"
        } else {
            if (serialRiseDays > 5) {
                "出现连续上涨"
            } else {
                ""
            }
        }

        if (result != "") {
            println("$code${StockHall.stockName(code)} 一共$totalDay 天，最大连续上涨$serialRiseDays 天，累计一共上涨$riseDays 天  $result")
        }
    }

    private fun serialDaysOfRise(list: List<StockHistory>): Int {
        var maxCount = 0
        var count = 0
        for (i in 0 until list.size) {
            if (list[i].stockPercent > 0.0) {
                count++
            } else {
                count = 0
            }
            maxCount = kotlin.math.max(maxCount, count)
        }
        return maxCount
    }
}