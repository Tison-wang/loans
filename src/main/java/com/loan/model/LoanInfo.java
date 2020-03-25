package com.loan.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:17
 */
@Data
public class LoanInfo extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 所属人编号
     */
    private Integer ownerId;
    /**
     * 借款人编号
     */
    private Integer userId;
    /**
     * 借款编号
     */
    private String no;
    /**
     * 借款金额
     */
    private BigDecimal amount;
    /**
     * 借款期限
     */
    private Integer loanTerm;
    /**
     * 借款用途
     */
    private String useFor;
    /**
     * 每月还款金额
     */
    private BigDecimal repayAmountOfMonth;
    /**
     * 打款图
     */
    private String paymentPic;
    /**
     * 备注
     */
    private String comment;
    /**
     * 当前提现金额
     */
    private BigDecimal currentWithdrawAmount;
    /**
     * 累计提现金额
     */
    private BigDecimal totalWithdrawAmount;
    /**
     * 状态描述
     */
    private String stateDesc;
    /**
     * 转账凭证
     */
    private String transferVoucher;
    /**
     * 合同
     */
    private String contract;
    /**
     * 服务费率
     */
    private double serviceChargeRate;

}
