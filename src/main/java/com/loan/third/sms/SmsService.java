package com.loan.third.sms;

/**
 * @description:
 * @author:
 * @time: 2019/12/15 23:28
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param mobile    手机号
     * @param content   内容
     * @param isSmsCode 是否为发送短信验证码 true：短信验证码 false：业务通知短信
     * @return
     */
    boolean sendMessage(String mobile, String content, boolean isSmsCode);

}
