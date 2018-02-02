package org.profit.persist.mapper.stock;

import org.apache.ibatis.annotations.Param;
import org.profit.persist.domain.stock.Daily;

import java.util.List;

/**
 * @author TangYing
 */
public interface DailyMapper {

    /**
     * 根据时间获取日常数据
     *
     * @param code
     * @param begin
     * @param end
     * @return
     */
    public List<Daily> selectByCode(@Param("code") String code,
                                    @Param("begin") String begin,
                                    @Param("end") String end);

    /**
     * 指定日期获取日常数据
     *
     * @param code
     * @param day
     * @return
     */
    Daily selectLastObj(@Param("code") String code, @Param("day") String day);

    /**
     * 插入日常数据
     *
     * @param list
     */
    public void insertList(List<Daily> list);
}
