package com.loan.model;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/12/2 23:43
 */
@Data
public class Wallet extends BaseModel {
    private static final long serialVersionUID = 151180437186391921L;
    /**
     * 用户编号
     */
    private Integer userId;
    /**
     * 钱包余额
     */
    private String amount;
    /**
     * 冻结金额
     */
    private String freezeAmount;
}
