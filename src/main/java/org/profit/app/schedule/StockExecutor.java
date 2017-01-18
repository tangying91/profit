package org.profit.app.schedule;

import org.profit.app.logic.magic.SimpleAnalyzer;
import org.profit.app.realm.StockHall;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum StockExecutor {

	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(StockExecutor.class);

	private ExecutorService service;

	public void start() {
		service = Executors.newFixedThreadPool(4);
	}

	public void shutdown() {
		service.shutdown();
	}

	public void repairHistory(final Stock stock, final String day) {
		service.submit(new Runnable() {
			@Override
			public void run() {
				try {
					List<Pool> pools = StockHall.getDayPools(stock.getCode(), day);
					if (pools.isEmpty()) {
						SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer(stock, day);
						simpleAnalyzer.analytic();

						LOG.info("Stock {} day {} is analytic.", stock.getCode(), day);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
