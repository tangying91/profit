package org.profit.persist.mapper;

import org.profit.persist.domain.Stock;

import java.util.List;

/**
 * @author TangYing
 */
public interface StockMapper {

    List<Stock> selectAll();

    Stock select(String code);

    void insert(Stock stock);

    void update(Stock stock);
}
