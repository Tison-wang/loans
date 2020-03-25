package com.loan.third.sms;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.loan.model.WebSiteConfig;
import com.loan.util.DateUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * @description: 短信发送工具
 * @author:
 * @time: 2019/12/15 17:53
 */
public final class SmsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsUtil.class);

    private SmsUtil() {
    }

    /**
     * 发送短信
     * <p>
     * userid	企业id	企业ID
     * timestamp	时间戳	系统当前时间字符串，年月日时分秒，例如：20120701231212
     * sign	签名	使用 账号+密码+时间戳 生成MD5字符串作为签名。MD5生成32位，且需要小写
     * 例如：
     * 账号是test
     * 密码是mima
     * 时间戳是20120701231212
     * 就需要用testmima20120701231212
     * 来生成MD5的签名，生成的签名为5cc68982f55ac74348e3d819f868fbe1
     * mobile	全部被叫号码	发信发送的目的号码.多个号码之间用半角逗号隔开
     * content	发送内容	短信的内容，内容需要UTF-8编码
     * sendTime	定时发送时间	为空表示立即发送，定时发送格式2010-10-24 09:08:10
     * action	发送任务命令	设置为固定的:send
     * extno	扩展子号	请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多10位数。
     * rt	接口类型	固定值 json，不填则为XML格式返回
     * action=send&rt=json&userid=12&timestamp=20120701231212&sign =5cc68982f55ac74348e3d819f868fbe1&
     * mobile=15023239810,13527576163&content=内容&sendTime=&extno=
     *
     * @param mobile    手机号
     * @param content   短信内容
     * @param isSmsCode 是否为发送短信验证码 true：短信验证码 false：业务通知短信
     * @return
     */
    public static boolean sendMessage(@NotNull String mobile, String content, boolean isSmsCode) {
        final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
        String smsUrl, smsUserId, smsAccount, smsPassword;
        if (isSmsCode) {
            smsUrl = webSiteConfig.getCodeSmsUrl();
            smsUserId = webSiteConfig.getCodeSmsUserId();
            smsAccount = webSiteConfig.getCodeSmsAccount();
            smsPassword = webSiteConfig.getCodeSmsPassword();
        } else {
            smsUrl = webSiteConfig.getNotifySmsUrl();
            smsUserId = webSiteConfig.getNotifySmsUserId();
            smsAccount = webSiteConfig.getNotifySmsAccount();
            smsPassword = webSiteConfig.getNotifySmsPassword();
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", "send");
        params.put("rt", "json");
        params.put("userid", smsUserId);
        String timestamp = DateUtil.dateTimeNoSymbol();
        params.put("timestamp", timestamp);
        params.put("sign", SecureUtil.md5((smsAccount + smsPassword + timestamp).toLowerCase()));
        params.put("mobile", mobile);
        params.put("content", content);
        params.put("sendTime", null);
        final String result;
        try {
            result = HttpUtil.post(smsUrl, params);
        } catch (Exception e) {
            LOGGER.error("短信接口请求失败！", e);
            return false;
        }
        final SendResult sendResult = JSONUtil.toBean(result, SendResult.class);
        LOGGER.info(sendResult.toString());
        if (StrUtil.equals(sendResult.getReturnStatus(), "Success")) {
            LOGGER.info("【短信发送成功】{}：{}", mobile, content);
            return true;
        }
        LOGGER.error("【短信发送失败】{}", sendResult.toString());
        return false;
    }
}
