package org.profit.persist.mapper.stock;

import org.apache.ibatis.annotations.Param;
import org.profit.persist.domain.stock.Daily;

import java.util.List;

/**
 * @author TangYing
 */
public interface DailyMapper {

    public List<Daily> selectByCode(@Param("code") String code,
                                    @Param("begin") String begin,
                                    @Param("end") String end);

    Daily selectLastObj(@Param("code") String code, @Param("day") String day);

    public void insertList(List<Daily> list);
}
