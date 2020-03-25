package com.loan.util;

/**
 * @description: 常量池
 * @author:
 * @time: 2019/11/25 22:40
 */
public final class Constants {

    private Constants() {
    }

    /**
     * 密码验证表达式 非空字符6-16位
     */
    public static final String PASSWORD_REG = "^\\S{6,16}$";
    /**
     * 手机号验证表达式
     */
    public static final String MOBILE_REG = "^1\\d{10}$";
    /**
     * 图片验证码缓存key前缀
     */
    public static final String IMAGE_CODE_PREFIX = "code:image:";
    /**
     * 注册短信验证码缓存key前缀
     */
    public static final String REGISTER_SMS_CODE_PREFIX = "code:sms:register:";
    /**
     * 忘记密码短信验证码缓存key前缀
     */
    public static final String FORGET_PWD_SMS_CODE_PREFIX = "code:sms:forget_pwd:";
    /**
     * 图片验证码缓存失效时间 3分钟
     */
    public static final long IMAGE_CODE_TIME_OUT = 60 * 5;
    /**
     * 短信验证码缓存失效时间5分钟
     */
    public static final long MSG_CODE_TIME_OUT = 60 * 5;
    /**
     * 订单编号前缀
     */
    public static final String ORDER_NO_PREFIX = "IA";
    /**
     * 合同管理-合同管理类型
     */
    public static final int CONTRACT_MANAGE_TYPE = 1;
    /**
     * 合同管理-支付页面类型
     */
    public static final int PAY_PAGE_TYPE = 2;
    /**
     * 注册验证码模板
     */
    public static final String REGISTER_SMS_TEMPLATE = "【%s】欢迎注册%s，您本次的验证码为%s，5分钟内有效。若非本人操作，请勿理会。";
    /**
     * 忘记密码验证码模板
     */
    public static final String FORGET_PWD_SMS_TEMPLATE = "【%s】您正在进行密码找回，本次的验证码为%s，5分钟内有效。若非本人操作，请勿理会。";
    /**
     * 后台访问地址前缀
     */
    public static final String BACK_URL_PREFIX = "/back";
    /**
     * 前端访问地址前缀
     */
    public static final String FRONT_URL_PREFIX = "/front";
    /**
     * 短信发送数量缓存前缀
     */
    public static final String SMS_COUNT_CACHE_PREFIX = "sms_count_";
    /**
     * accessToken缓存过期时间 30分钟
     */
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60;
}
