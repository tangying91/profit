package org.profit.app.schedule

import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 线程池
 *
 * @author TangYing
 */
object StockExecutor {

    private val LOG = LoggerFactory.getLogger(StockExecutor::class.java)
    private var service: ExecutorService? = null

    fun start() {
        service = Executors.newFixedThreadPool(4)
        LOG.info("StockExecutor is started.")
    }

    fun shutdown() {
        service?.shutdown()
    }
}