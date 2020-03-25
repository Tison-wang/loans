package com.loan.dao;

import com.loan.model.UserDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/24 22:13
 */
@Mapper
public interface UserDetailMapper {

    /**
     * 通过用户编号获取用户详情信息
     *
     * @param userId
     * @return
     */
    UserDetail getUserDetail(Integer userId);

    /**
     * 更新用户详情信息
     *
     * @param params
     * @return
     */
    int update(Map<String, Object> params);

    /**
     * 新增用户详情信息
     *
     * @param userDetail
     * @return
     */
    int insert(UserDetail userDetail);
}
