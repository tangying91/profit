package org.profit.app.analyse

import org.profit.app.pojo.StockHistory
import org.profit.util.DateUtils
import org.profit.util.FileUtils
import java.lang.Exception

/**
 * 股票数据分析器
 */
abstract class StockAnalyzer(val code: String) {

    abstract fun analyse()

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

//    /**
//     * 分析股票
//     *
//     * @param type
//     * @param day
//     */
//    private fun analytic(type: Int, day: String?) {
//        val pool = StockStat()
//        pool.type = type
//        pool.day = day
//        pool.code = stock.getCode()
//        if (stockFoundList!!.size < type || stockDailyList!!.size < type) {
//            return
//        }
//        // Step1: 分析上涨趋势
//        analyticUpwardTrend(pool, type)
//        // Step2: 分析量比参数
//        analyticVolume(pool, type)
//        // Step3: 分析资金进出
//        analyticFoundTrend(pool, type)
//        // Insert
//        StockHall.insertPool(pool)
//    }
//
//    /**
//     * 分析上涨趋势
//     *
//     * @param pool
//     * @param day
//     */
//    private fun analyticUpwardTrend(pool: StockStat, day: Int) { // 获取天数
//        var day = day
//        day = if (stockDailyList!!.size > day) day else stockDailyList!!.size
//        // 截取数据
//        val list = ListUtils.sublist(stockDailyList, 0, day)
//        // 最小值
//        var min = stockDailyLatest.getLow()
//        // 最大值
//        var max = stockDailyLatest.getHigh()
//        // 上涨天数
//        var riseDay = 0
//        for (i in 0 until day) { // 当天
//            val today = list!![i]
//            // 当天的收盘价大于开盘价
//            if (today.getClose() >= today.getOpen()) {
//                riseDay++
//            }
//            min = Math.min(min, today.getLow())
//            max = Math.max(max, today.getHigh())
//        }
//        // 最大振幅  周期内(最高价 - 最低价)
//        val amplitude = if (min > 0) (max - min) / min else 0.0
//        // 涨幅  周期内(最后一天收盘价 - 第一天开盘价)
//        var gains = 0.0
//        val dailyLong = list!![list.size - 1]
//        if (dailyLong != null && dailyLong.open > 0) {
//            gains = (stockDailyLatest.getClose() - dailyLong.open) / dailyLong.open
//        }
//        // 赋值
//        pool.riseDay = riseDay
//        pool.amplitude = amplitude
//        pool.gains = gains
//        LOG.debug("Download {} analytic successful.", pool.code)
//    }
//
//    /**
//     * 分析成交量
//     *
//     * @param pool
//     * @param day
//     */
//    private fun analyticVolume(pool: StockStat, day: Int) { // 获取天数
//        var day = day
//        day = if (stockDailyList!!.size > day) day else stockDailyList!!.size
//        // 截取数据
//        val list = ListUtils.sublist(stockDailyList, 0, day)
//        // 一段周期的总成交量
//        var volumeTotal = 0L
//        for (daily in list!!) {
//            volumeTotal += daily.getVolume()
//        }
//        // 最后一天和一段周期的平均量比
//        var volumeRate = 0.0
//        // 除最近一天外的成交量平均数除以最近一天的成交量
//        if (volumeTotal > 0) {
//            val avgVolume = (volumeTotal - stockDailyLatest.getVolume()) * 1.0 / (day - 1)
//            volumeRate = stockDailyLatest.getVolume() * 1.0 / avgVolume
//        }
//        // 赋值
//        pool.volumeRate = volumeRate
//        pool.volumeTotal = volumeTotal
//    }
//
//    /**
//     * 分析资金趋势
//     *
//     * @param pool
//     * @param day
//     */
//    private fun analyticFoundTrend(pool: StockStat, day: Int) { // 获取天数
//        var day = day
//        day = if (stockFoundList!!.size > day) day else stockFoundList!!.size
//        var totalMin = 0
//        var totalMid = 0
//        var totalBig = 0
//        var totalMax = 0
//        for (i in 0 until day) {
//            val found = stockFoundList!![i]
//            totalMin += found.getMin().toInt()
//            totalMid += found.getMid().toInt()
//            totalBig += found.getBig().toInt()
//            totalMax += found.getMax().toInt()
//        }
//        // 赋值
//        pool.minInflow = totalMin.toDouble()
//        pool.midInflow = totalMid.toDouble()
//        pool.bigInflow = totalBig.toDouble()
//        pool.maxInflow = totalMax.toDouble()
//        pool.inflowAvg = (totalMin + totalMid + totalBig + totalMax) * 1.0 / day
//    }
//
//    companion object {
//        private val LOG = LoggerFactory.getLogger(SimpleAnalyzer::class.java)
//    }

}