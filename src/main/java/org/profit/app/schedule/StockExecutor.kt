package org.profit.app.schedule

import org.profit.app.service.DownloadHistoryService
import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 线程池
 */
object StockExecutor {

    private val service: ExecutorService = Executors.newFixedThreadPool(8)

    fun shutdown() {
        service.shutdown()
    }

    fun download(code: String, date: String) {
        service.submit{
            println("$code 开始下载 $code 的数据...")
            DownloadHistoryService(code, date).execute()
        }
    }
}