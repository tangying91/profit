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
 * 游戏大厅
 */
public class StockHall {

    private static final Logger LOG = LoggerFactory.getLogger(StockHall.class);

    // Database mapper
    private static final StockMapper stockMapper = (StockMapper) AppContext.getBean(PersistContext.STOCK_MAPPER);
    private static final PoolMapper poolMapper = (PoolMapper) AppContext.getBean(PersistContext.STOCK_POOL_MAPPER);
    private static final FoundMapper foundMapper = (FoundMapper) AppContext.getBean(PersistContext.STOCK_FOUND_MAPPER);
    private static final DailyMapper dailyMapper = (DailyMapper) AppContext.getBean(PersistContext.STOCK_DALIY_MAPPER);

    public static void insert(Stock stock) {
        stockMapper.insert(stock);
    }

    public static void update(Stock stock) {
        try {
            stockMapper.update(stock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Stock> getAllStocks() {
        return stockMapper.selectAll();
    }

    public static Stock getStock(String code) {
        return stockMapper.select(code);
    }

    public static List<Pool> getDayPools(String code, String day) {
        return poolMapper.selectDayPools(code, day);
    }

    public static Daily getLastDailyObj(String code, String day) {
        return dailyMapper.selectLastObj(code, day);
    }

    public static Found getLastFoundObj(String code, String day) {
        return foundMapper.selectLastObj(code, day);
    }


    private static Map<String, String> nameMap = new HashMap<String, String>();

    public static void initName() {
        nameMap.clear();

        List<Stock> stockList = getAllStocks();
        for(Stock stock : stockList) {
            nameMap.put(stock.getCode(), stock.getName());
        }
    }

    public static String getStockName(String code) {
        return nameMap.containsKey(code) ? nameMap.get(code) : "";
    }

    public static void deletePool(String code) {
        poolMapper.delete(code);
    }

    public static void insertPool(Pool pool) {
        try {
            poolMapper.insert(pool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Found> selectFounds(String code, String endDay) {
        return foundMapper.selectList(code, "2015-01-01", endDay);
    }

    public static List<Daily> selectDailies(String code, String endDay) {
        return dailyMapper.selectByCode(code, "2015-01-01", endDay);
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
            LOG.info("Download {} found successful. But no needs add records. {} ", code, list.size());
            return;
        }

        foundMapper.insertList(list);
        LOG.info("Download {} found successful. Add {} new records", code, list.size());
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
            LOG.info("Download {} daily successful. But no needs add records. {} ", code, list.size());
            return;
        }

        dailyMapper.insertList(list);
        LOG.info("Download {} daily successful. Add {} new records", code, list.size());
    }
}
