package com.loan.third.aliyun;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.loan.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * @description: 阿里云短信发送工具
 * @author:
 * @time: 2019/12/3 21:28
 */
public final class AliyunSmsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunSmsUtil.class);

    private AliyunSmsUtil() {
    }

    /**
     * 发送短信
     *
     * @param mobile   手机号
     * @param account
     * @param template
     * @return
     */
    public static boolean sendMessage(@NotNull String mobile, @NotNull AliyunAccount account, @NotNull AliyunSmsTemplate template) {
        if (!mobile.matches(Constants.MOBILE_REG)) {
            return false;
        }
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", account.getAccessKeyId(), account.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", template.getSignName());
        request.putQueryParameter("TemplateCode", template.getTemplateCode());
        request.putQueryParameter("TemplateParam", template.getTemplateParam());

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            LOGGER.error("【短信发送失败】", e);
        }

        return true;
    }
}
