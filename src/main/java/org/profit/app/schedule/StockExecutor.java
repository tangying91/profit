package org.profit.app.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 *
 * @author TangYing
 */
public enum StockExecutor {

	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(StockExecutor.class);

	private ExecutorService service;

	public void start() {
		service = Executors.newFixedThreadPool(4);
		LOG.info("StockExecutor is started.");
	}

	public void shutdown() {
		service.shutdown();
	}
}
