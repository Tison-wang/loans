package com.loan.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author:
 * @time: 2019/12/8 18:49
 */
public interface IWalletService {
    /**
     * 获取金额和冻结金额
     *
     * @param userId
     * @return amount:金额 freezeAmount:冻结金额
     */
    Optional<Map<String, BigDecimal>> getAmountAndFreezeAmount(int userId);

    /**
     * 减少提现额度同时增加冻结金额
     *
     * @param userId
     * @return
     */
    boolean subtractAmountAndAddFreezeAmount(int userId, BigDecimal amount, BigDecimal freezeAmount);

    /**
     * 增加提现额度
     *
     * @param userId
     * @param amount
     * @return
     */
    boolean addAmount(int userId, BigDecimal amount);

    /**
     * 减少提现额度
     *
     * @param userId
     * @param amount
     * @return
     */
    boolean subtractAmount(int userId, BigDecimal amount);

    /**
     * 增加冻结金额
     *
     * @param userId
     * @param amount
     * @return
     */
    boolean addFreezeAmount(int userId, BigDecimal amount);

    /**
     * 减少冻结金额
     *
     * @param userId
     * @param amount
     * @return
     */
    boolean subtractFreezeAmount(int userId, BigDecimal amount);

    /**
     * 获取用户提现额度
     *
     * @param userId
     * @return
     */
    Optional<BigDecimal> getAmount(int userId);
}
