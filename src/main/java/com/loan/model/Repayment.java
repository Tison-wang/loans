package com.loan.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author:
 * @time: 2019/12/5 0:32
 */
@Data
public class Repayment extends BaseModel {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    /**
     * 借款账单编号
     */
    private Integer loanId;
    /**
     * 借款账单编码
     */
    private String loanNo;
    /**
     * 还款金额
     */
    private BigDecimal amount;
    /**
     * 当前周期
     */
    private int currentPeriod;
    /**
     * 总共周期
     */
    private int totalPeriod;
}
