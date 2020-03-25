package com.loan.dao;

import com.loan.model.SystemConfig;
import com.loan.model.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/25 21:37
 */
@Mapper
public interface SystemConfigMapper {

    /**
     * 获取单个系统配置
     *
     * @param params
     * @return
     */
    SystemConfig getSingleSystemConfig(Map<String, Object> params);

    /**
     * 更新系统配置
     *
     * @param config
     * @return
     */
    int update(SystemConfig config);

    /**
     * 更新系统配置
     *
     * @param fields
     * @return
     */
    int update(Map<String, Object> fields);

    /**
     * 添加系统配置
     *
     * @param config
     * @return
     */
    int insert(SystemConfig config);
}
