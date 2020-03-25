package com.loan.service;

import com.github.pagehelper.PageInfo;
import com.loan.model.Withdraw;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/5 0:34
 */
public interface IWithdrawService {
    /**
     * 增加提现记录
     *
     * @param withdraw
     * @return
     */
    boolean save(Withdraw withdraw);

    /**
     * 提现
     *
     * @param withdrawAmount
     * @return
     */
    boolean withdraw(BigDecimal withdrawAmount);

    /**
     * 获取提现列表
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<Withdraw> getWithdrawByPage(int pageNum, int pageSize, Map<String, Object> params);

    /**
     * 判断用户是否有未处理的提现记录
     *
     * @param userId
     * @return <code>true</code> 有未处理的提现记录
     * <code>false</code> 没有未处理的提现记录
     */
    boolean hasUnProcessWithdrawRecord(int userId);

    /**
     * 更新提现记录
     *
     * @param params
     * @return
     */
    boolean update(Map<String, Object> params);
}
