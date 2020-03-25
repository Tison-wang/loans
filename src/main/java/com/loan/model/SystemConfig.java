package com.loan.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 21:09
 */
@Data
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 站点编号
     */
    private Integer id;
    /**
     * 站点所属人
     */
    private Integer ownerId;
    /**
     * 站点域名
     */
    private String domain;
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 站点标题
     */
    private String siteTitle;
    /**
     * 站点关键字
     */
    private String siteKeyword;
    /**
     * 站点描述
     */
    private String siteDesc;
    /**
     * 站点关闭状态
     */
    private Boolean closeStatus;
    /**
     * 站点关闭提示
     */
    private String closedTips;
    /**
     * 统计/客服代码
     */
    private String statisticalCode;
    /**
     * 备案信息
     */
    private String recordInfo;
    /**
     * 短信签名
     */
    private String smsSign;
    /**
     * 首页模板
     */
    private String indexTemplate;
    /**
     * 总共短信条数
     */
    private Long totalSmsCount;
    /**
     * 已使用短信条数
     */
    private Long costSmsCount;
    /**
     * 状态 -1：删除 默认为1
     */
    private Integer status;
    /**
     * 底部内容
     */
    private String bottomContent;

    //=========================贷款设置================================
    /**
     * 最小贷款金额
     */
    private BigDecimal minLoan;
    /**
     * 最大贷款金额
     */
    private BigDecimal maxLoan;
    /**
     * 默认贷款金额
     */
    private BigDecimal defaultLoan;
    /**
     * 允许选择的贷款月份
     */
    private String allowLoanMonths;
    /**
     * 默认贷款月份
     */
    private Integer defaultLoanMonth;
    /**
     * 服务费率
     */
    private Double serviceChargeRate;
    /**
     * 每月还款日
     */
    private Integer repaymentDate;
}
