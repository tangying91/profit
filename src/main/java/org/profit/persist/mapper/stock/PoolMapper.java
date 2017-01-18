package org.profit.persist.mapper.stock;

import org.apache.ibatis.annotations.Param;
import org.profit.persist.domain.stock.Pool;

import java.util.List;

/**
 * @author TangYing
*/
public interface PoolMapper {

    public List<Pool> selectPools(@Param("day") String day,
                                  @Param("type") int type,
                                  @Param("riseDay") int riseDay,
                                  @Param("volumeRate") double volumeRate,
                                  @Param("gainsMin") double gainsMin,
                                  @Param("gainsMax") double gainsMax,
                                  @Param("min") double min,
                                  @Param("mid") double mid,
                                  @Param("big") double big,
                                  @Param("max") double max,
                                  @Param("start") int start,
                                  @Param("limit") int limit);

    public List<Pool> selectDayPools(@Param("code") String code, @Param("day") String day);

    void insert(Pool pool);

    void insertList(List<Pool> list);

    void delete(String code);
}
