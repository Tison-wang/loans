package com.loan.dao;

import com.loan.model.Contract;
import com.loan.model.Contract;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/1 19:02
 */
@Mapper
public interface ContractMapper {

    /**
     * 获取合同列表
     *
     * @param params
     * @return
     */
    List<Contract> getList(Map<String, Object> params);

    /**
     * 更新合同信息
     *
     * @param params
     * @return
     */
    int update(Map<String, Object> params);

    /**
     * 添加合同
     *
     * @param contract
     * @return
     */
    int insert(Contract contract);

}
