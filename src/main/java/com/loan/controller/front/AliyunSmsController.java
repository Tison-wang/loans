package com.loan.controller.front;

import cn.hutool.json.JSONUtil;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.WebSiteConfig;
import com.loan.third.aliyun.AliyunAccount;
import com.loan.third.aliyun.AliyunSmsTemplate;
import com.loan.third.aliyun.AliyunSmsUtil;
import com.loan.util.Constants;
import com.loan.util.RandomUtil;
import com.loan.util.RedisUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.model.WebSiteConfig;
import com.loan.third.aliyun.AliyunAccount;
import com.loan.third.aliyun.AliyunSmsTemplate;
import com.loan.third.aliyun.AliyunSmsUtil;
import com.loan.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 阿里云短信服务
 * @author:
 * @time: 2019/12/4 21:07
 */
@RestController
@RequestMapping("/api/front/aliyunSms")
public class AliyunSmsController extends BaseController {
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/{mobile}/getCode")
    public JsonResult getCode(@PathVariable String mobile) {
        final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
        if (!webSiteConfig.getOpenCodeStatus()) {
            return fail(CodeEnum.FORBIDDEN);
        }
        if (!mobile.matches(Constants.MOBILE_REG)) {
            return fail(CodeEnum.INCORRECT_MOBILE);
        }
        Map<String, Object> params = new HashMap<>();
        final String code = RandomUtil.generatorSixNumber();
        params.put("code", code);
        final AliyunSmsTemplate template = new AliyunSmsTemplate(webSiteConfig.getSmsSignName(), JSONUtil.toJsonStr(params));
        final boolean result = AliyunSmsUtil.sendMessage(mobile, new AliyunAccount(webSiteConfig.getSmsAccessKeyId(), webSiteConfig.getSmsAccessSecret()), template);
        if (result) {
            redisUtil.set(Constants.REGISTER_SMS_CODE_PREFIX + mobile, code, Constants.MSG_CODE_TIME_OUT);
            return success();
        }
        return fail();
    }
}
