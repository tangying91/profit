package org.profit.persist.mapper.stock;

import org.apache.ibatis.annotations.Param;
import org.profit.persist.domain.stock.Found;

import java.util.List;

/**
 * @author TangYing
 */
public interface FoundMapper {

    public List<Found> selectList(@Param("code") String code,
                                  @Param("begin") String begin,
                                  @Param("end") String end);

    Found selectLastObj(@Param("code") String code, @Param("day") String day);

    public void insertList(List<Found> list);
}
