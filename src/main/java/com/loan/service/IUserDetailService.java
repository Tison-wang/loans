package com.loan.service;

import com.loan.model.UserDetail;

import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 22:43
 */
public interface IUserDetailService {

    /**
     * 新增用户详情信息
     *
     * @param userDetail
     * @return
     */
    boolean addUserDetail(UserDetail userDetail);

    /**
     * 通过用户编号获取用户详情信息
     *
     * @param userId
     * @return
     */
    UserDetail getUserDetailByUserId(Integer userId);

    /**
     * 更新用户详情信息
     *
     * @param params
     * @return
     */
    boolean update(Map<String, Object> params);

    /**
     * 更新用户详情信息
     *
     * @param detail
     * @return
     */
    boolean update(UserDetail detail);

}
