package com.loan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loan.dao.RepaymentMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.Repayment;
import com.loan.service.IRepaymentService;
import com.loan.exception.BizException;
import com.loan.service.IRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/5 0:29
 */
@Service
public class RepaymentServiceImpl implements IRepaymentService {

    @Autowired
    private RepaymentMapper repaymentMapper;

    @Override
    public boolean addRepayment(Repayment repayment) {
        if (repayment == null || repayment.getUserId() == null) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        //默认添加订单时，还款期数为第一期,划款状态为未还款
        repayment.setCurrentPeriod(1);
        repayment.setState(0);
        repayment.setCreateTime(new Date());
        return repaymentMapper.insert(repayment) > 0;
    }

    @Override
    public PageInfo<Repayment> getRepaymentByPage(int pageNum, int pageSize, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        PageHelper.startPage(pageNum, pageSize);
        final List<Repayment> repayments = repaymentMapper.selectList(params);
        return new PageInfo<>(repayments);
    }
}
