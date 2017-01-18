package org.profit.app.logic.magic;

import org.profit.app.Consts;
import org.profit.app.realm.StockHall;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Daily;
import org.profit.persist.domain.stock.Found;
import org.profit.persist.domain.stock.Pool;
import org.profit.util.DateUtils;
import org.profit.util.ListUtils;

import java.util.List;

public class SimpleAnalyzer {

    private List<Daily> dailyList;
    private List<Found> foundList;
    private Daily dailyLatest;
    private Stock stock;
    private String day;

    public SimpleAnalyzer(Stock stock, String day) {
        this.stock = stock;
        this.day = day;
    }

    public void analytic() {
        // 检查这一天是否是节假日
        if (DateUtils.isWeek(day)) {
            return;
        }

        // 检查数据是否完整
        Found foundLast = StockHall.getLastFoundObj(stock.getCode(), day);
        dailyLatest = StockHall.getLastDailyObj(stock.getCode(), day);
        if (foundLast == null || dailyLatest == null) {
            return;
        }

        this.dailyList = StockHall.selectDailies(stock.getCode(), day);
        this.foundList = StockHall.selectFounds(stock.getCode(), day);

        // Analytic

        // 寻找短期爆发点
        analytic(Consts.ANALYTIC_5_DAY, day);
        analytic(Consts.ANALYTIC_10_DAY, day);
        // 寻找底部区域
        analytic(Consts.ANALYTIC_30_DAY, day);
    }

    public void analytic(int type, String day) {
        Pool pool = new Pool();
        pool.setType(type);
        pool.setDay(day);
        pool.setCode(stock.getCode());

        if (foundList.size() < type || dailyList.size() < type) {
            return;
        }

        // Step1: 分析上涨趋势
        analyticUpwardTrend(pool, type);

        // Step2: 分析量比参数
        analyticVolume(pool, type);

        // Step3: 分析资金进出
        analyticFoundTrend(pool, type);

        // Insert
        StockHall.insertPool(pool);
    }

    /**
     * 分析上涨趋势
     */
    public void analyticUpwardTrend(Pool pool, int day) {
        // 获取天数
        day = dailyList.size() > day ? day :dailyList.size();

        // 截取数据
        List<Daily> list = ListUtils.sublist(dailyList, 0, day);

        // 最小值
        double min = dailyLatest.getLow();
        // 最大值
        double max = dailyLatest.getHigh();

        // 上涨天数
        int riseDay = 0;
        for (int i = 0; i < day; i++) {
            // 当天
            Daily today = list.get(i);
            // 当天的收盘价大于开盘价
            if (today.getClose() >= today.getOpen()) {
                riseDay++;
            }
            min = Math.min(min, today.getLow());
            max = Math.max(max, today.getHigh());
        }

        // 最大振幅  周期内(最高价 - 最低价)
        double amplitude = min > 0 ? (max - min) / min : 0.0;

        // 涨幅  周期内(最后一天收盘价 - 第一天开盘价)
        double gains = 0.0;
        Daily dailyLong = list.get(list.size() - 1);
        if (dailyLong != null && dailyLong.getOpen() > 0 ) {
            gains = (dailyLatest.getClose() - dailyLong.getOpen()) / dailyLong.getOpen();
        }

        // 赋值
        pool.setRiseDay(riseDay);
        pool.setAmplitude(amplitude);
        pool.setGains(gains);
    }

    /**
     * 分析成交量
     */
    public void analyticVolume(Pool pool, int day) {
        // 获取天数
        day = dailyList.size() > day ? day :dailyList.size();

        // 截取数据
        List<Daily> list = ListUtils.sublist(dailyList, 0, day);

        // 一段周期的总成交量
        long volumeTotal = 0L;
        for (Daily daily : list) {
            volumeTotal += daily.getVolume();
        }

        // 最后一天和一段周期的平均量比
        double volumeRate = 0.0;

        // 除最近一天外的成交量平均数除以最近一天的成交量
        if (volumeTotal > 0) {
            double avgVolume = (volumeTotal - dailyLatest.getVolume()) * 1.0 / (day - 1);
            volumeRate = dailyLatest.getVolume() * 1.0 / avgVolume;
        }

        // 赋值
        pool.setVolumeRate(volumeRate);
        pool.setVolumeTotal(volumeTotal);
    }

    /**
     * 分析资金趋势
     */
    public void analyticFoundTrend(Pool pool, int day) {
        // 获取天数
        day = foundList.size() > day ? day :foundList.size();

        int totalMin = 0;
        int totalMid = 0;
        int totalBig = 0;
        int totalMax = 0;

        for (int i = 0; i < day; i++) {
            Found found = foundList.get(i);
            totalMin += found.getMin();
            totalMid += found.getMid();
            totalBig += found.getBig();
            totalMax += found.getMax();
        }

        // 赋值
        pool.setMinInflow(totalMin);
        pool.setMidInflow(totalMid);
        pool.setBigInflow(totalBig);
        pool.setMaxInflow(totalMax);
        pool.setInflowAvg((totalMin + totalMid + totalBig + totalMax) * 1.0 / day);
    }
}
