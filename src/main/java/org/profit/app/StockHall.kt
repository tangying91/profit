package org.profit.app

import org.profit.app.service.DownloadDailyService
import org.profit.util.DateUtils
import org.slf4j.LoggerFactory
import java.util.*

/**
 * 数据大厅
 * 选股数据模型
 *
 * 1、【积】周期内,连续上涨天数较多
 * 2、【金】周期内,资金持续流入天数较多
 * 3、【突】周期末,股价成交量大幅拉升
 * 4、【底】较长周期内涨幅较低,持续底部整理
*/
object StockHall {

    private val LOG = LoggerFactory.getLogger(StockHall::class.java)

    /**
     * 按顺序开始处理当天的数据
     */
    fun start() {
        val date = DateUtils.formatDate(System.currentTimeMillis())
        allStockCodes.forEach { code ->
            DownloadDailyService(code, date).execute()
//            DownloadFoundService(code, date).execute()
        }

//        startDownloadDaily(day)
//        startDownloadFound(day)
//        startAnalytic(day)
    }

    /**
     * 开始分析数据
     */
    fun startAnalytic(day: String) {
        allStockCodes.forEach { code ->
            // 获取该股票的所有数据，进行数据分析

//            val pools = StockHall.getDayPools(stock.code, day)
//            if (pools!!.isEmpty()) {
//                val simpleAnalyzer = SimpleAnalyzer(stock, day)
//                simpleAnalyzer.analytic()
//                stock.analyticed = true
//                StockHall.update(stock)
//                try {
//                    Thread.sleep(IntegerUtils.generateRandomSeed(500, 5000).toLong())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
        }
    }

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
}