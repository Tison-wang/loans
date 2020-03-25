package com.loan.third.sms;

import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.SystemConfig;
import com.loan.service.ISystemConfigService;
import com.loan.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 短信服务适配器
 * @author:
 * @time: 2019/12/16 22:14
 */
@Component
public class SmsServiceAdapter implements SmsService {
    private SmsService smsService;
    @Autowired
    private ISystemConfigService systemConfigService;

    private SystemConfig systemConfig;

    public SmsServiceAdapter setSmsService(SmsService smsService) {
        this.smsService = smsService;
        return this;
    }

    public SmsServiceAdapter setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
        return this;
    }

    @Override
    public boolean sendMessage(String mobile, String content, boolean isSmsCode) {
        if (systemConfig.getTotalSmsCount() <= systemConfig.getCostSmsCount()) {
            throw new BizException(CodeEnum.SMS_SERVICE_FORBIDDEN);
        }
        final boolean result = smsService.sendMessage(mobile, content, isSmsCode);
        if (result) {
            SystemConfig localSystemConfig = new SystemConfig();
            localSystemConfig.setId(systemConfig.getId());
            localSystemConfig.setCostSmsCount(systemConfig.getCostSmsCount() + 1);
            systemConfigService.updateSystemConfig(localSystemConfig);
        }
        return result;
    }
}
