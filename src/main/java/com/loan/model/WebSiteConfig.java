package com.loan.model;

import lombok.Data;

@Data
public class WebSiteConfig {
    private Integer id;
    /**
     * 短信签名
     */
    private String smsSignName;
    /**
     * 短信accessKeyId
     */
    private String smsAccessKeyId;
    /**
     * 短信accessSecret
     */
    private String smsAccessSecret;
    /**
     * 是否开启短信验证码
     */
    private Boolean openCodeStatus;
    /**
     * 站点短信接口每天访问次数限制
     */
    private Integer siteCountLimit;
    /**
     * 验证码短信接口地址
     */
    private String codeSmsUrl;
    /**
     * 验证码短信用户id
     */
    private String codeSmsUserId;
    /**
     * 验证码短信账号
     */
    private String codeSmsAccount;
    /**
     * 验证码短信账号的密码
     */
    private String codeSmsPassword;
    /**
     * 通知类短信接口地址
     */
    private String notifySmsUrl;
    /**
     * 通知类短信用户id
     */
    private String notifySmsUserId;
    /**
     * 通知类短信账号
     */
    private String notifySmsAccount;
    /**
     * 通知类短信账号的密码
     */
    private String notifySmsPassword;
    /**
     * 总共短信条数
     */
    private Long totalSmsCount;
    /**
     * 已使用短信条数
     */
    private Long costSmsCount;

}