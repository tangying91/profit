package org.profit.persist.mapper.sys;

import org.apache.ibatis.annotations.Param;
import org.profit.persist.domain.sys.User;

/**
 * @author TangYing
 */
public interface UserMapper {

	User login(@Param("account") String account, @Param("password") String password);

	void insert(User user);

    void update(User user);
}
