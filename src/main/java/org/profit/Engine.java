package org.profit;

import org.profit.app.realm.StockHall;
import org.profit.app.schedule.StockExecutor;
import org.profit.config.AppContext;
import org.profit.server.services.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TangYing
 */
public enum Engine {

	INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

	private ScheduleService scheduleService = (ScheduleService) AppContext.getBean(AppContext.SCHEDULE_SERVICE);

	public void start() {
		// 初始化多任务
		scheduleService.init();
		StockHall.loadName();
		LOG.info("Stock Engine started successfully.");
    }

	public void stop() {
		StockExecutor.INSTANCE.shutdown();
		LOG.warn("StockExecutor is shutdown.");

		scheduleService.destroy();
		LOG.warn("ScheduleService is destroyed.");
	}
}