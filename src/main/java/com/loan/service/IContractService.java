package com.loan.service;

import com.loan.model.Contract;
import com.loan.model.Contract;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/1 19:22
 */
public interface IContractService {
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
    boolean update(Map<String, Object> params);

    /**
     * 获取单个合同信息
     *
     * @param params
     * @return
     */
    Contract getSingleContract(Map<String, Object> params);

    /**
     * 添加合同
     *
     * @param contract
     * @return
     */
    boolean addContract(Contract contract);
}
