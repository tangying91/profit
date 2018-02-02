package org.profit.app.realm;

import org.profit.config.PersistContext;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Daily;
import org.profit.persist.domain.stock.Found;
import org.profit.persist.domain.stock.Pool;
import org.profit.persist.mapper.StockMapper;
import org.profit.persist.mapper.stock.DailyMapper;
import org.profit.persist.mapper.stock.FoundMapper;
import org.profit.persist.mapper.stock.PoolMapper;
import org.profit.config.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 股票数据大厅
 *
 * @author TangYing
 */
public class StockHall {

    private static final Logger LOG = LoggerFactory.getLogger(StockHall.class);

    private static final StockMapper STOCK_MAPPER = (StockMapper) AppContext.getBean(PersistContext.STOCK_MAPPER);
    private static final PoolMapper STOCK_POOL_MAPPER = (PoolMapper) AppContext.getBean(PersistContext.STOCK_POOL_MAPPER);
    private static final FoundMapper STOCK_FOUND_MAPPER = (FoundMapper) AppContext.getBean(PersistContext.STOCK_FOUND_MAPPER);
    private static final DailyMapper STOCK_DAILY_MAPPER = (DailyMapper) AppContext.getBean(PersistContext.STOCK_DALIY_MAPPER);

    private static Map<String, String> nameMap = new HashMap<String, String>();

    public static void loadName() {
        List<Stock> stocks = getAllStocks();
        for (Stock stock : stocks) {
            nameMap.put(stock.getCode(), stock.getName());
        }
    }

    public static String getStockName(String code) {
        return nameMap.containsKey(code) ? nameMap.get(code) : "";
    }

    /**
     * 插入股票
     *
     * @param stock
     */
    public static void insert(Stock stock) {
        STOCK_MAPPER.insert(stock);
    }

    /**
     * 更新股票
     *
     * @param stock
     */
    public static void update(Stock stock) {
        try {
            STOCK_MAPPER.update(stock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有股票数据
     *
     * @return
     */
    public static List<Stock> getAllStocks() {
        return STOCK_MAPPER.selectAll();
    }

    /**
     * 获取指定股票数据
     *
     * @param code
     * @return
     */
    public static Stock getStock(String code) {
        return STOCK_MAPPER.select(code);
    }

    /**
     * 获取指定股票和日期的股票池
     *
     * @param code
     * @param day
     * @return
     */
    public static List<Pool> getDayPools(String code, String day) {
        return STOCK_POOL_MAPPER.selectDayPools(code, day);
    }

    /**
     * 获取指定股票日常基础数据
     *
     * @param code
     * @param day
     * @return
     */
    public static Daily getLastDailyObj(String code, String day) {
        return STOCK_DAILY_MAPPER.selectLastObj(code, day);
    }

    /**
     * 获取指定股票资金数据
     *
     * @param code
     * @param day
     * @return
     */
    public static Found getLastFoundObj(String code, String day) {
        return STOCK_FOUND_MAPPER.selectLastObj(code, day);
    }

    /**
     * 删除指定股票的股票池
     *
     * @param code
     */
    public static void deletePool(String code) {
        STOCK_POOL_MAPPER.delete(code);
    }

    /**
     * 插入数据到股票池
     *
     * @param pool
     */
    public static void insertPool(Pool pool) {
        try {
            STOCK_POOL_MAPPER.insert(pool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Found> selectFounds(String code, String endDay) {
        return STOCK_FOUND_MAPPER.selectList(code, "2015-01-01", endDay);
    }

    public static List<Daily> selectDailies(String code, String endDay) {
        return STOCK_DAILY_MAPPER.selectByCode(code, "2015-01-01", endDay);
    }

    /**
     * 保存股票资金数据
     * 并保证数据不重复
     */
    public static void saveNoRepeatedFoundList(String code, List<Found> list) {
        // No repeat operation
        List<Found> localList = selectFounds(code, "2050-01-01");
        list.removeAll(localList);

        if (list.isEmpty()) {
            LOG.debug("Download {} found successful. But no needs add records. {} ", code, list.size());
            return;
        }

        STOCK_FOUND_MAPPER.insertList(list);
        LOG.debug("Download {} found successful. Add {} new records", code, list.size());
    }

    /**
     * 保存股票基础数据
     * 并保证数据不重复
     */
    public static void saveNoRepeatedDailyList(String code, List<Daily> list) {
        // No repeat operation
        List<Daily> localList = selectDailies(code, "2050-01-01");
        list.removeAll(localList);

        if (list.isEmpty()) {
            LOG.debug("Download {} daily successful. But no needs add records. {} ", code, list.size());
            return;
        }

        STOCK_DAILY_MAPPER.insertList(list);
        LOG.debug("Download {} daily successful. Add {} new records", code, list.size());
    }
}
