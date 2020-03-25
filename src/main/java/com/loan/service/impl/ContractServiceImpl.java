package com.loan.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.loan.dao.ContractMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.Contract;
import com.loan.service.BaseService;
import com.loan.service.IContractService;
import com.loan.dao.ContractMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.Contract;
import com.loan.service.BaseService;
import com.loan.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/1 19:25
 */
@Service
public class ContractServiceImpl extends BaseService implements IContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public List<Contract> getList(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return contractMapper.getList(params);
    }

    @Override
    public boolean update(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return contractMapper.update(params) > 0;
    }

    @Override
    public Contract getSingleContract(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        List<Contract> list = getList(params);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Override
    public boolean addContract(Contract contract) {
        if (contract == null) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        contract.setCreateTime(new Date());
        return contractMapper.insert(contract) > 0;
    }
}
