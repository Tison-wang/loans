package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.ThreadLocalCache;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/30 22:07
 */
@Controller
@RequestMapping("/api/back/userCenter")
@RequireLogin
public class BackUserCenterController extends BaseController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/getDomain")
    @ResponseBody
    public JsonResult getDomain() {
        final SystemConfig config = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        if (config == null || StrUtil.isBlank(config.getDomain())) {
            return fail(CodeEnum.SITE_NOT_INITIAL);
        }
        return success(config.getDomain());
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping("/update/password")
    public JsonResult updatePassword(String oldPassword, String newPassword) {
        if (StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (!StrUtil.equals(user.getPassword(), SecureUtil.md5(oldPassword))) {
            return fail(CodeEnum.PASSWORD_NOT_CORRECT);
        }
        if (!newPassword.matches(Constants.PASSWORD_REG)) {
            return fail(CodeEnum.PASSWORD_FORMAT_ERROR);
        }
        String password = SecureUtil.md5(newPassword);
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("password", password);
        boolean result = userService.update(params);
        if (result) {
            user.setPassword(password);
            ThreadLocalCache.put(ThreadLocalCache.KeyEnum.USER, user);
            return success();
        }
        return fail();
    }
}
