package org.profit.app

import org.profit.app.analyse.StockUpAnalyzer
import org.profit.app.service.DownloadHistoryService
import org.profit.util.DateUtils
import org.profit.util.FileUtils
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
        // 读取当天的日志记录，看是否已经下载过数据
        val logs = FileUtils.readLogs()
        val date = DateUtils.formatDate(System.currentTimeMillis())
        allStockCodes.filter { !logs.contains(it) }.forEach { code ->
            println("$code 开始下载数据...")
            DownloadHistoryService(code, date).execute()
//            Thread.sleep((Math.random() * 5000 + 5000).toLong())
        }

        // 分析数据
        allStockCodes.forEach {
            StockUpAnalyzer(it).analyse()
        }
    }

    /**
     * 股票池
     * key -> code
     * value -> name
     */
    private val stockMap: MutableMap<String, String> = HashMap()

    init {
        FileUtils.readStocks().forEach {
            val array = it.split(" ")
            stockMap[array[0]] = array[1]
        }
    }

    /**
     * 所有股票代码
     */
    val allStockCodes get() = stockMap.keys

    /**
     * 获取名称
     */
    fun stockName(code: String): String {
        return stockMap[code] ?: ""
    }
}