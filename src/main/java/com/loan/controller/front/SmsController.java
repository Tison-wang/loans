package com.loan.controller.front;

import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.SystemConfig;
import com.loan.model.WebSiteConfig;
import com.loan.third.sms.SmsServiceAdapter;
import com.loan.third.sms.ThirdSmsService;
import com.loan.util.Constants;
import com.loan.util.RandomUtil;
import com.loan.util.RedisUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.third.sms.ThirdSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @description:
 * @author:
 * @time: 2019/12/15 23:20
 */
@RestController
@RequestMapping("/api/front/sms")
public class SmsController extends BaseController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SmsServiceAdapter smsServiceAdapter;

    @GetMapping("/{mobile}/{type}/getCode")
    public JsonResult getCode(@PathVariable String mobile, @PathVariable Integer type) {
        final int[] types = {1, 2};
        if (Collections.singletonList(types).contains(type)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
        if (!webSiteConfig.getOpenCodeStatus()) {
            return fail(CodeEnum.FORBIDDEN);
        }
        if (!mobile.matches(Constants.MOBILE_REG)) {
            return fail(CodeEnum.INCORRECT_MOBILE);
        }
        final SystemConfig systemConfig = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        final String smsSign = systemConfig.getSmsSign();
        final String code = RandomUtil.generatorSixNumber();
        String content = null;
        if (type == 1) {
            content = String.format(Constants.REGISTER_SMS_TEMPLATE, smsSign, smsSign, code);
        } else if (type == 2) {
            content = String.format(Constants.FORGET_PWD_SMS_TEMPLATE, smsSign, code);
        }
        final boolean result = smsServiceAdapter.setSmsService(new ThirdSmsService()).setSystemConfig(systemConfig).sendMessage(mobile, content, true);
        if (result) {
            if (type == 1) {
                redisUtil.set(Constants.REGISTER_SMS_CODE_PREFIX + mobile, code, Constants.MSG_CODE_TIME_OUT);
            } else if (type == 2) {
                redisUtil.set(Constants.FORGET_PWD_SMS_CODE_PREFIX + mobile, code, Constants.MSG_CODE_TIME_OUT);
            }
            return success();
        }
        return fail();
    }
}
