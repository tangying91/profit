package org.profit.app.realm;

import org.profit.app.logic.magic.SimpleAnalyzer;
import org.profit.app.logic.magic.SimpleDailyDownloadFromSina;
import org.profit.app.logic.magic.SimpleFoundDownload;
import org.profit.app.logic.Filter;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Daily;
import org.profit.persist.domain.stock.Found;
import org.profit.persist.domain.stock.Pool;
import org.profit.app.schedule.StockExecutor;
import org.profit.config.StockProperties;
import org.profit.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 选股数据模型
 *
 * 1、【积】周期内,连续上涨天数较多
 * 2、【金】周期内,资金持续流入天数较多
 * 3、【突】周期末,股价成交量大幅拉升
 * 4、【底】较长周期内涨幅较低,持续底部整理
 */
public class StockRobot {

    private static final Logger LOG = LoggerFactory.getLogger(StockRobot.class);

    public static void run() {
        if (!StockProperties.download) {
            return;
        }

        String lastDay = DateUtils.formatDate(System.currentTimeMillis());

        List<Stock> stocks = StockHall.getAllStocks();
        for (Stock stock : stocks) {
            if (stock.getLastAnalyticTime() == DateUtils.getZeroTime()) {
                continue;
            }

            // Run
            run(stock, lastDay);

            // Update
            stock.setLastAnalyticTime(DateUtils.getZeroTime());
            StockHall.update(stock);
        }
    }

    public static void run(Stock stock, String lastDay) {
        // 1、基础数据下载补偿
        step1(stock, lastDay);
        LOG.info("Stock {} daily download completed...", stock.getCode());

        // 2、资金数据下载补偿
        step2(stock, lastDay);
        LOG.info("Stock {} found download completed...", stock.getCode());

        // 3、数据分析生成
        step3(stock, lastDay);
        LOG.info("Stock {} analytic completed...", stock.getCode());

        // 4、修复分析
        step4(stock, lastDay);
    }

    public static void step1(Stock stock, String day) {
        // 查看最新的数据,判断是否要下载
        Daily lastObj = StockHall.getLastDailyObj(stock.getCode(), day);
        if (lastObj != null) {
            // 已经有数据,则不重复下载
            if (lastObj.getDay().equals(day)) {
                return;
            }

            // 检查数据是否日期溢出
            long lastTime = DateUtils.parseTime(lastObj.getDay());
            long dayTime = DateUtils.parseTime(day);
            if (lastTime > dayTime) {
                return;
            }
        }

        List<Daily> dailyList = new ArrayList<Daily>();
        SimpleDailyDownloadFromSina simpleDailyDownload = new SimpleDailyDownloadFromSina(stock);
        List<Daily> list = simpleDailyDownload.execute();

        // 过滤没有成交量的数据
        for (Daily daily : list) {
            if (Filter.INSTANCE.filterVolume(daily)) {
                dailyList.add(daily);
            }
        }

        // 检查基础数据是否为空
        if (dailyList.isEmpty()) {
            return;
        }

        // 保存数据
        StockHall.saveNoRepeatedDailyList(stock.getCode(), dailyList);
    }

    public static void step2(Stock stock, String day) {
        // 查看最新的数据,判断是否要下载
        Found lastObj = StockHall.getLastFoundObj(stock.getCode(), day);
        if (lastObj != null) {
            // 已经有数据,则不重复下载
            if (lastObj.getDay().equals(day)) {
                return;
            }

            // 检查数据是否日期溢出
            long lastTime = DateUtils.parseTime(lastObj.getDay());
            long dayTime = DateUtils.parseTime(day);
            if (lastTime > dayTime) {
                return;
            }
        }

        List<Found> foundList = new ArrayList<Found>();

        // Step2: 资金数据下载
        SimpleFoundDownload simpleFoundDownload = new SimpleFoundDownload(stock);
        foundList = simpleFoundDownload.execute();

        // 保存数据
        StockHall.saveNoRepeatedFoundList(stock.getCode(), foundList);

    }

    public static void step3(Stock stock, String day) {
        // 分析该日期的数据
        List<Pool> pools = StockHall.getDayPools(stock.getCode(), day);
        if (pools.isEmpty()) {
            SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer(stock, day);
            simpleAnalyzer.analytic();
        }
    }

    public static void step4(Stock stock, String day) {
        // 尝试修复最近半个月的
        long end = DateUtils.parseTime(day);
        long begin = end - DateUtils.MILLISECOND_PER_DAY * 30;
        for (long b = begin; b < end; b += DateUtils.MILLISECOND_PER_DAY) {
            String d = DateUtils.formatDate(b);
            // 检查这一天是否是节假日
            if (!DateUtils.isWeek(d)) {
                StockExecutor.INSTANCE.repairHistory(stock, d);
            }
        }
    }
}
