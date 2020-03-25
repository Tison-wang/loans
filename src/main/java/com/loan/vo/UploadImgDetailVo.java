package com.loan.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/12/19 21:49
 */
@Data
@Builder
public class UploadImgDetailVo {
    /**
     * 转账批次号
     */
    private String batchNumber;
    /**
     * 转出单位
     */
    private String transferCompany;
    /**
     * 转出账户
     */
    private String transferAccount;
    /**
     * 转出账号地区
     */
    private String transferArea;
    /**
     * 收款人姓名
     */
    private String receiptName;
    /**
     * 收款银行
     */
    private String receiptBank;
    /**
     * 收款账户
     */
    private String receiptBankAccount;
    /**
     * 币种
     */
    private String currency;
    /**
     * 转账金额
     */
    private String transferAmount;
    /**
     * 转账时间
     */
    private String transferDate;
    /**
     * 转账类型
     */
    private String transferType;
    /**
     * 执行方式
     */
    private String executeWay;
    /**
     * 状态
     */
    private String status;
    /**
     * 银行备注
     */
    private String bankRemark;
    /**
     * 处理结果
     */
    private String dealResult;
    /**
     * 用户备注
     */
    private String customRemark;
}
