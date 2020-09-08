package org.profit.app.realm

import org.slf4j.LoggerFactory
import java.util.*

/**
 * 数据大厅
 */
object StockHall {

    private val LOG = LoggerFactory.getLogger(StockHall::class.java)

    /**
     * 股票池
     * key -> code
     * value -> name
     */
    private val stockMap: MutableMap<String, String> = HashMap()

    init {
        stockMap["002594"] = "比亚迪"
        stockMap["002456"] = "欧菲光"
    }

    /**
     * 所有股票代码
     */
    val allStockCodes get() = stockMap.keys

//    /**
//     * 获取指定股票和日期的股票池
//     *
//     * @param code
//     * @param day
//     * @return
//     */
//    fun getDayPools(code: String?, day: String?): List<StockStat?>? {
//        return STOCK_POOL_MAPPER!!.selectDayPools(code, day)
//    }
//
//    /**
//     * 获取指定股票日常基础数据
//     *
//     * @param code
//     * @param day
//     * @return
//     */
//    fun getLastDailyObj(code: String?, day: String?): StockDaily? {
//        return STOCK_DAILY_MAPPER!!.selectLastObj(code, day)
//    }
//
//    /**
//     * 获取指定股票资金数据
//     *
//     * @param code
//     * @param day
//     * @return
//     */
//    fun getLastFoundObj(code: String?, day: String?): StockFound? {
//        return STOCK_FOUND_MAPPER!!.selectLastObj(code, day)
//    }
//
//    fun selectFounds(code: String?, endDay: String?): List<StockFound?>? {
//        return STOCK_FOUND_MAPPER!!.selectList(code, "2015-01-01", endDay)
//    }
//
//    fun selectDailies(code: String?, endDay: String?): List<StockDaily?>? {
//        return STOCK_DAILY_MAPPER!!.selectByCode(code, "2015-01-01", endDay)
//    }

//    /**
//     * 保存股票资金数据
//     * 并保证数据不重复
//     */
//    fun saveNoRepeatedFoundList(code: String?, list: List<StockFound?>?) { // No repeat operation
//        val localList = selectFounds(code, "2050-01-01")
//        list.removeAll(localList)
//        if (list!!.isEmpty()) {
//            LOG.debug("Download {} found successful. But no needs add records. {} ", code, list.size)
//            return
//        }
//        STOCK_FOUND_MAPPER!!.insertList(list)
//        LOG.debug("Download {} found successful. Add {} new records", code, list.size)
//    }
//
//    /**
//     * 保存股票基础数据
//     * 并保证数据不重复
//     */
//    fun saveNoRepeatedDailyList(code: String?, list: List<StockDaily?>) { // No repeat operation
//        val localList = selectDailies(code, "2050-01-01")
//        list.removeAll(localList)
//        if (list.isEmpty()) {
//            LOG.debug("Download {} daily successful. But no needs add records. {} ", code, list.size)
//            return
//        }
//        STOCK_DAILY_MAPPER!!.insertList(list)
//        LOG.debug("Download {} daily successful. Add {} new records", code, list.size)
//    }
}