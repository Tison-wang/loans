package com.loan.dao;

import com.loan.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/24 22:13
 */
@Mapper
public interface UserMapper {

    /**
     * 获取单个用户
     *
     * @param params
     * @return
     */
    User getSingleUser(Map<String, Object> params);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 获取用户列表
     *
     * @param params
     * @return
     */
    List<User> getUserList(Map<String, Object> params);

    /**
     * 更新用户信息
     *
     * @param params
     * @return
     */
    int update(Map<String, Object> params);
}
