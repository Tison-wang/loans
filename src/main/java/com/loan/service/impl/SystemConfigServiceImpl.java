package com.loan.service.impl;

import com.loan.dao.SystemConfigMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.SystemConfig;
import com.loan.service.ISystemConfigService;
import com.loan.exception.BizException;
import com.loan.model.SystemConfig;
import com.loan.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/25 21:36
 */
@Service
public class SystemConfigServiceImpl implements ISystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public SystemConfig getSingleSystemConfigById(Integer id) {
        if (id == null || id < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return systemConfigMapper.getSingleSystemConfig(params);
    }

    @Override
    public SystemConfig getSingleSystemConfigByOwnerId(Integer ownerId) {
        if (ownerId == null || ownerId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ownerId", ownerId);
        return systemConfigMapper.getSingleSystemConfig(params);
    }

    @Override
    public boolean updateSystemConfig(SystemConfig config) {
        if (config == null || ((config.getId() == null || config.getId() < 1) && (config.getOwnerId() == null || config.getOwnerId() < 1))) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return systemConfigMapper.update(config) > 0;
    }

    @Override
    public boolean addSystemConfig(SystemConfig config) {
        if (config == null || config.getOwnerId() == null || config.getOwnerId() < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return systemConfigMapper.insert(config) > 0;
    }

    @Override
    public SystemConfig getSingleSystemConfig(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return systemConfigMapper.getSingleSystemConfig(params);
    }
}
