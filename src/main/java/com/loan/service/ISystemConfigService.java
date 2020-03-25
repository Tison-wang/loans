package com.loan.service;

import com.loan.model.SystemConfig;
import com.loan.model.SystemConfig;

import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/25 21:36
 */
public interface ISystemConfigService {

    /**
     * 通过id获取系统配置
     *
     * @param id
     * @return
     */
    SystemConfig getSingleSystemConfigById(Integer id);

    /**
     * 通过所属人编号获取系统配置
     *
     * @param ownerId
     * @return
     */
    SystemConfig getSingleSystemConfigByOwnerId(Integer ownerId);

    /**
     * 更新系统配置
     *
     * @param config
     * @return
     */
    boolean updateSystemConfig(SystemConfig config);

    /**
     * 添加系统配置
     *
     * @param config
     * @return
     */
    boolean addSystemConfig(SystemConfig config);

    /**
     * 获取单个系统配置信息
     *
     * @param params
     * @return
     */
    SystemConfig getSingleSystemConfig(Map<String, Object> params);
}
