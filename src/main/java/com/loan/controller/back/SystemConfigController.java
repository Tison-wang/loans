package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.service.ISystemConfigService;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:27
 */
@RestController
@RequestMapping("/api/back/sysConfig")
@RequireLogin
public class SystemConfigController extends BaseController {

    @Autowired
    private ISystemConfigService systemConfigService;

    @PostMapping("/view")
    public JsonResult<SystemConfig> view() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        SystemConfig config = systemConfigService.getSingleSystemConfigByOwnerId(user.getId());
        return success(config);
    }

    @PostMapping("/update")
    public JsonResult update(SystemConfig config) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        SystemConfig oldConfig = systemConfigService.getSingleSystemConfigByOwnerId(user.getId());
        // 域名校验
        if (!StrUtil.isBlank(config.getDomain()) && (oldConfig == null || (!StrUtil.isBlank(oldConfig.getDomain()) &&
                !StrUtil.equals(oldConfig.getDomain(), config.getDomain())))) {
            Map<String, Object> params = new HashMap<>();
            params.put("domain", config.getDomain());
            final SystemConfig systemConfig = systemConfigService.getSingleSystemConfig(params);
            if (systemConfig != null) {
                return fail(CodeEnum.SITE_HAS_EXISTED);
            }
        }
        // 最小金额，最大金额，初始金额校验
        if (config.getMinLoan() != null && config.getMinLoan().doubleValue() <= 0) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "最小贷款金额不能小于0！");
        }
        if (config.getMaxLoan() != null && config.getMaxLoan().doubleValue() <= 0) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "最大贷款金额不能小于0！");
        }
        if (config.getMinLoan() != null && config.getMaxLoan() != null && config.getMinLoan().doubleValue() > config.getMaxLoan().doubleValue()) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "最大贷款金额不能小于最小贷款金额！");
        }
        if (config.getMinLoan() != null && config.getMinLoan() != null && config.getMinLoan().doubleValue() > config.getDefaultLoan().doubleValue()) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "初始贷款金额不能小于最小贷款金额！");
        }
        if (config.getMinLoan() != null && config.getMinLoan() != null && config.getMaxLoan().doubleValue() < config.getDefaultLoan().doubleValue()) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "初始贷款金额不能大于最大贷款金额！");
        }
        config.setOwnerId(user.getId());
        if (oldConfig == null) {
            boolean result = systemConfigService.addSystemConfig(config);
            return result ? success() : fail();
        }
        boolean result = systemConfigService.updateSystemConfig(config);
        return result ? success() : fail();
    }
}
