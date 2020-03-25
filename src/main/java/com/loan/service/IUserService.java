package com.loan.service;

import com.github.pagehelper.PageInfo;
import com.loan.model.User;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 22:43
 */
public interface IUserService {

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    boolean addUser(User user);

    /**
     * 通过id获取用户
     *
     * @param id
     * @return
     */
    User getUserById(Integer id);

    /**
     * 通过用户名获取用户
     *
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 通过用户名和密码获取用户
     *
     * @param userName
     * @param password
     * @return
     */
    User getUserByUserNameAndPassword(String userName, String password);

    /**
     * 获取用户列表
     *
     * @param params
     * @return
     */
    List<User> getUserList(Map<String, Object> params);

    /**
     * 分页获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<User> getUserByPage(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 更新用户信息
     *
     * @param params
     * @return
     */
    boolean update(Map<String, Object> params);
}
