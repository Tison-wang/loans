package com.loan.service;

import com.github.pagehelper.PageInfo;
import com.loan.model.Repayment;
import com.loan.model.Withdraw;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/5 0:25
 */
public interface IRepaymentService {
    /**
     * 添加还款信息
     *
     * @param repayment
     * @return
     */
    boolean addRepayment(Repayment repayment);

    /**
     * 分页获取还款列表
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<Repayment> getRepaymentByPage(int pageNum, int pageSize, Map<String, Object> params);
}
