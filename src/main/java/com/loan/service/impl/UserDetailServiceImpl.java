package com.loan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.loan.dao.UserDetailMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.UserDetail;
import com.loan.service.IUserDetailService;
import com.loan.dao.UserDetailMapper;
import com.loan.exception.BizException;
import com.loan.service.IUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 22:44
 */
@Service
public class UserDetailServiceImpl implements IUserDetailService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Override
    public boolean addUserDetail(UserDetail userDetail) {
        if (userDetail == null || userDetail.getUserId() == null) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        userDetail.setCreateTime(new Date());
        return userDetailMapper.insert(userDetail) > 0;
    }

    @Override
    public UserDetail getUserDetailByUserId(Integer userId) {
        if (userId == null || userId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return userDetailMapper.getUserDetail(userId);
    }

    @Override
    public boolean update(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return userDetailMapper.update(params) > 0;
    }

    @Override
    public boolean update(UserDetail detail) {
        if (detail == null || (detail.getId() == null && detail.getUserId() == null)) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return update(BeanUtil.beanToMap(detail, false, true));
    }
}
