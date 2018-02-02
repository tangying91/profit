package org.profit.app.realm;

import org.profit.app.logic.Filter;
import org.profit.app.logic.magic.SimpleAnalyzer;
import org.profit.app.logic.magic.SimpleDailyDownloadFromSina;
import org.profit.app.logic.magic.SimpleFoundDownload;
import org.profit.app.schedule.StockExecutor;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Daily;
import org.profit.persist.domain.stock.Found;
import org.profit.persist.domain.stock.Pool;
import org.profit.util.DateUtils;
import org.profit.util.IntegerUtils;
import org.profit.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 选股数据模型
 *
 * 1、【积】周期内,连续上涨天数较多
 * 2、【金】周期内,资金持续流入天数较多
 * 3、【突】周期末,股价成交量大幅拉升
 * 4、【底】较长周期内涨幅较低,持续底部整理
 *
 * @author TangYing
 */
public class StockRobot {

    /**
     * 按顺序开始处理当天的数据
     */
    public static void start() {
        String day = DateUtils.formatDate(System.currentTimeMillis());
        StockRobot.startDownloadDaily(day);
        StockRobot.startDownloadFound(day);
        StockRobot.startAnalytic(day);
    }

    /**
     * 开始分析数据
     *
     * @param day
     */
    public static void startAnalytic(String day) {
        List<Stock> stocks = StockHall.getAllStocks();
        for (Stock stock : stocks) {
            List<Pool> pools = StockHall.getDayPools(stock.getCode(), day);
            if (pools.isEmpty()) {
                SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer(stock, day);
                simpleAnalyzer.analytic();

                stock.setAnalyticed(true);
                StockHall.update(stock);

                try {
                    Thread.sleep(IntegerUtils.generateRandomSeed(500, 5000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开始下载基础数据
     *
     * @param day
     */
    public static void startDownloadDaily(String day) {
        List<Stock> stocks = StockHall.getAllStocks();
        for (Stock stock : stocks) {
            if(downloadStockDaily(stock, day)) {
                try {
                    Thread.sleep(IntegerUtils.generateRandomSeed(500, 5000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开始下载资金数据
     *
     * @param day
     */
    public static void startDownloadFound(String day) {
        List<Stock> stocks = StockHall.getAllStocks();
        for (Stock stock : stocks) {
            if(downloadStockFound(stock, day)) {
                try {
                    Thread.sleep(IntegerUtils.generateRandomSeed(500, 5000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean downloadStockDaily(Stock stock, String day) {
        // 查看最新的数据,判断是否要下载
        Daily lastObj = StockHall.getLastDailyObj(stock.getCode(), day);
        if (lastObj != null) {
            // 已经有数据,则不重复下载
            if (lastObj.getDay().equals(day)) {
                return false;
            }

            // 检查数据是否日期溢出
            long lastTime = DateUtils.parseTime(lastObj.getDay());
            long dayTime = DateUtils.parseTime(day);
            if (lastTime > dayTime) {
                return false;
            }
        }

        List<Daily> dailyList = new ArrayList<Daily>();
        SimpleDailyDownloadFromSina simpleDailyDownload = new SimpleDailyDownloadFromSina(stock);
        List<Daily> list = simpleDailyDownload.execute();

        stock.setDownloadedBase(true);
        StockHall.update(stock);

        // 过滤没有成交量的数据
        for (Daily daily : list) {
            if (Filter.INSTANCE.filterVolume(daily)) {
                dailyList.add(daily);
            }
        }

        // 检查基础数据是否为空
        if (dailyList.isEmpty()) {
            return true;
        }

        // 保存数据
        StockHall.saveNoRepeatedDailyList(stock.getCode(), dailyList);
        return true;
    }

    private static boolean downloadStockFound(Stock stock, String day) {
        // 查看最新的数据,判断是否要下载
        Found lastObj = StockHall.getLastFoundObj(stock.getCode(), day);
        if (lastObj != null) {
            // 已经有数据,则不重复下载
            if (lastObj.getDay().equals(day)) {
                return false;
            }

            // 检查数据是否日期溢出
            long lastTime = DateUtils.parseTime(lastObj.getDay());
            long dayTime = DateUtils.parseTime(day);
            if (lastTime > dayTime) {
                return false;
            }
        }

        List<Found> foundList = new ArrayList<Found>();

        // Step2: 资金数据下载
        SimpleFoundDownload simpleFoundDownload = new SimpleFoundDownload(stock);
        foundList = simpleFoundDownload.execute();

        stock.setDownloadedFund(true);
        StockHall.update(stock);

        // 保存数据
        StockHall.saveNoRepeatedFoundList(stock.getCode(), foundList);

        return true;
    }
}
