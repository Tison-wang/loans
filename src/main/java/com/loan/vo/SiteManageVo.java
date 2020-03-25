package com.loan.vo;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/12/19 21:49
 */
@Data
public class SiteManageVo {
    /**
     * 系统配置编号
     */
    private Integer systemConfigId;
    /**
     * 业务员
     */
    private String admin;
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 域名
     */
    private String domain;
    /**
     * 短信签名
     */
    private String smsSign;
    /**
     * 首页模板
     */
    private String indexTemplate;
    /**
     * 站点关闭状态 true:关闭 false：未关闭
     */
    private Boolean siteClosedStatus;
    /**
     * 总短信条数
     */
    private long totalSmsCount;
    /**
     * 已使用短信条数
     */
    private long costSmsCount;
}
