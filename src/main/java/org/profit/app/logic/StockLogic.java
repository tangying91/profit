package org.profit.app.logic;

import org.profit.app.realm.StockHall;
import org.profit.persist.domain.Stock;

public class StockLogic {

    public static void generateStockCode() {
//        String[] headers = new String[] {"600", "601", "000"};
        String[] headers = new String[] {"300", "002"};
        for (int i = 0; i < 999; i++) {
            for (String header : headers) {
                String code = header + i;
                if (i < 10) {
                    code = header + "00" + i;
                } else if (i < 100) {
                    code = header + "0" + i;
                }

                Stock stock = StockHall.getStock(code);
                if (stock == null) {
                    // 新建对象
                    Stock item = new Stock();
                    item.setName("");
                    item.setCode(code);
                    item.setLastAnalyticTime(0L);
                    StockHall.insert(item);
                }
            }
        }
    }
}
