package com.loan.third.sms;

import cn.hutool.core.util.StrUtil;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author:
 * @time: 2019/12/15 23:28
 */
public class ThirdSmsService implements SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdSmsService.class);

    /**
     * 发送短信
     *
     * @param mobile    手机号
     * @param content   内容
     * @param isSmsCode 是否为发送短信验证码 true：短信验证码 false：业务通知短信
     * @return
     */
    @Override
    public boolean sendMessage(String mobile, String content, boolean isSmsCode) {
        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(content)) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        if (!mobile.matches(Constants.MOBILE_REG)) {
            throw new BizException(CodeEnum.INCORRECT_MOBILE);
        }
        return SmsUtil.sendMessage(mobile, content, isSmsCode);
    }
}
