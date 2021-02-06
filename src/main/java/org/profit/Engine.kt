package org.profit

import org.profit.app.analyse.StockMockTrading
import org.profit.app.schedule.StockExecutor
import org.profit.config.AppContext
import org.profit.server.services.ScheduleService
import org.slf4j.LoggerFactory

/**
 * @author TangYing
 */
enum class Engine {

    INSTANCE;

    private val scheduleService = AppContext.Companion.getBean(AppContext.Companion.SCHEDULE_SERVICE) as ScheduleService

    fun start() {
        scheduleService.init()
        LOG.info("Stock Engine started successfully.")

        test()
    }

    fun stop() {
        StockExecutor.shutdown()
        LOG.warn("StockExecutor is shutdown.")

        scheduleService.destroy();
        LOG.warn("ScheduleService is destroyed.")
    }

    fun test() {
//        StockHall.download()
//        StockHall.analyse()

        StockMockTrading("002185").analyse()
//        StockMockTrading("000932").analyse()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(Engine::class.java)
    }
}