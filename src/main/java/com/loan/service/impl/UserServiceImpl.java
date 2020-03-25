package com.loan.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loan.dao.UserMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.SpringBeanUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.dao.UserMapper;
import com.loan.exception.BizException;
import com.loan.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 22:44
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean addUser(User user) {
        if (user == null) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setCreateTime(new Date());
        return userMapper.insert(user) > 0;
    }

    @Override
    public User getUserById(Integer id) {
        if (id == null || id < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return userMapper.getSingleUser(params);
    }

    @Override
    public User getUserByUserName(String userName) {
        if (StrUtil.isBlank(userName)) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        final SystemConfig config = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        params.put("ownerId", config.getOwnerId());
        params.put("userName", userName);
        return userMapper.getSingleUser(params);
    }

    @Override
    public User getUserByUserNameAndPassword(String userName, String password) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        String servletPath = SpringBeanUtil.getRequest().getServletPath();
        if (servletPath.startsWith(Constants.FRONT_URL_PREFIX)) {
            final SystemConfig config = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
            params.put("ownerId", config.getOwnerId());
        }
        params.put("userName", userName);
        params.put("password", password);
        return userMapper.getSingleUser(params);
    }

    @Override
    public List<User> getUserList(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return userMapper.getUserList(params);
    }

    @Override
    public PageInfo<User> getUserByPage(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = getUserList(params);
        return new PageInfo<>(userList);
    }

    @Override
    public boolean update(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return userMapper.update(params) > 0;
    }
}
