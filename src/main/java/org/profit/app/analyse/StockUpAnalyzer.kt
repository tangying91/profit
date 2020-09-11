package org.profit.app.analyse

import org.profit.app.StockHall
import org.profit.app.pojo.StockHistory
import org.profit.util.DateUtils
import org.profit.util.FileUtils
import java.lang.Exception

/**
 * 【积】周期内,连续上涨天数较多
 */
class StockUpAnalyzer(code: String, private val statCount: Int) : StockAnalyzer(code) {

    /**
     * 1.周期内总天数
     * 2.周期内连续上涨最大天数
     * 3.周内总上涨天数
     */
    override fun analyse() {
        // 创业板暂时不筛选
        if (code.startsWith("3")) {
            return
        }

        // ST股票不筛选
        if (StockHall.stockName(code).contains("ST")) {
            return
        }

        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 如果没有数据
        if (list.isEmpty()) {
            return
        }

        val totalDay = list.size
        val riseDays = list.count { it.close > it.open }
        if (riseDays >= totalDay) {
            println("$code${StockHall.stockName(code)} 一共$totalDay 天，累计一共上涨$riseDays 天")
            StockVolumeAnalyzer(code, statCount).analyse()
        }
    }
}