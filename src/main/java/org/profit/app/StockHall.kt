package org.profit.app

import org.profit.app.analyse.StockVolumeAnalyzer
import org.profit.app.schedule.StockExecutor
import org.profit.util.DateUtils
import org.profit.util.FileUtils
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

    /**
     * 下载数据
     */
    fun download() {
        // 读取当天的日志记录，看是否已经下载过数据
        val logs = FileUtils.readLogs()
        val date = DateUtils.formatDate(System.currentTimeMillis())
        allStockCodes.filter {
            !logs.contains(it)
                    && !it.startsWith("3")
                    && !it.startsWith("688")
        }.forEach { code ->
//            DownloadHistoryService(code, date).execute()
            StockExecutor.download(code, date)
        }
    }

    fun analyse() {
        val results = mutableListOf<String>()

        // 分析股价数据
        allStockCodes.filter {
            !it.startsWith("3")
                    && !it.startsWith("688")
        }.forEach {
//            StockDownAnalyzer(it, 20, 0.8).analyse()
//            StockHistoryAnalyzer(it, 180, 0.05).analyse()
            StockVolumeAnalyzer(it, 10, 2.0).analyse(results)
        }
        println("数据分析完成.")

        // 邮件
        if (results.isNotEmpty()) {
            val content = StringBuffer()

            results.forEach {
                println(it)
                content.append(it).append("\n")
            }

            EMailSender.send("457151376@qq.com", DateUtils.formatDate(System.currentTimeMillis()), content.toString())
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