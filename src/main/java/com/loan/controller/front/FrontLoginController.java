package com.loan.controller.front;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.jsonFilter.JSON;
import com.loan.model.JsonResult;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.model.WebSiteConfig;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.RedisUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.model.WebSiteConfig;
import com.loan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:23
 */
@Controller
@RequestMapping("/api/front")
public class FrontLoginController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @JSON(type = User.class, filter = "password,detail,amount,freezeAmount,lastLoginTime,modifier,modifyTime,createTime")
    public JsonResult<User> login(String userName, String password) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        final User user = userService.getUserByUserName(userName);
        if (user == null) {
            return fail(CodeEnum.ACCOUNT_NOT_EXIST);
        }
        if (user.getState() == 0) {
            return fail(CodeEnum.ACCOUNT_FORBIDDEN);
        }
        if (!StrUtil.equals(user.getPassword(), SecureUtil.md5(password))) {
            return fail(CodeEnum.LOGIN_FAIL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("lastLoginTime", new Date());
        userService.update(params);
        //生成token
        String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
        redisUtil.set(accessToken, user.getId(), Constants.ACCESS_TOKEN_EXPIRE_TIME);
        final JsonResult<User> result = success(user);
        Map<String, Object> extendData = new HashMap<>();
        extendData.put("accessToken", accessToken);
        result.setExtendData(extendData);
        return result;
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public JsonResult logout(HttpServletRequest request) {
        final String accessToken = request.getHeader("accessToken");
        if (!StrUtil.isBlank(accessToken)) {
            redisUtil.del(accessToken);
        }
        ThreadLocalCache.clear();
        return success();
    }

    /**
     * 获取验证码开启状态
     *
     * @return
     */
    @RequestMapping("/openCodeStatus")
    @ResponseBody
    public JsonResult openCodeStatus() {
        final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
        return success(webSiteConfig.getOpenCodeStatus());
    }

    /**
     * 注册
     *
     * @param userName
     * @param password
     * @param code
     * @return
     */
    @RequestMapping("/logUp")
    @ResponseBody
    public JsonResult logUp(String userName, String password, String code, String smsCode) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password) || StrUtil.isBlank(code)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        if (!password.matches(Constants.PASSWORD_REG)) {
            return fail(CodeEnum.PASSWORD_FORMAT_ERROR);
        }
        // 获取缓存中的图片验证码
        String cacheCode = (String) redisUtil.get(Constants.IMAGE_CODE_PREFIX + code.toLowerCase());
        if (cacheCode == null || !StrUtil.equals(cacheCode, code.toLowerCase())) {
            return fail(CodeEnum.IMAGE_CODE_ERROR);
        }
        if (!StrUtil.isBlank(smsCode)) {
            final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
            if (webSiteConfig.getOpenCodeStatus()) {
                //获取缓存中的短信验证码
                final String cacheSmsCode = (String) redisUtil.get(Constants.REGISTER_SMS_CODE_PREFIX + userName);
                if (cacheSmsCode == null || !StrUtil.equals(cacheSmsCode, smsCode)) {
                    return fail(CodeEnum.SMS_CODE_ERROR);
                }
            }
        }
        final SystemConfig config = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        final User user = userService.getUserByUserName(userName);
        if (user != null) {
            return fail(CodeEnum.PHONE_NUMBER_HAS_LOG_UP);
        }
        User newUser = new User();
        newUser.setOwnerId(config.getOwnerId());
        newUser.setUserName(userName);
        newUser.setPassword(password);
        newUser.setType(0);
        newUser.setState(1);
        return userService.addUser(newUser) ? success() : fail();
    }

    /**
     * 重置密码
     *
     * @param userName
     * @param password
     * @param code
     * @return
     */
    @RequestMapping("/resetPassword")
    @ResponseBody
    public JsonResult resetPassword(HttpServletRequest request, String userName, String password, String code, String smsCode) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password) || StrUtil.isBlank(code)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        User user = userService.getUserByUserName(userName);
        if (user == null) {
            return fail(CodeEnum.PHONE_NUMBER_NOT_LOG_UP);
        }
        if (!password.matches(Constants.PASSWORD_REG)) {
            return fail(CodeEnum.PASSWORD_FORMAT_ERROR);
        }
        String cacheCode = (String) redisUtil.get(Constants.IMAGE_CODE_PREFIX + code.toLowerCase());
        if (cacheCode == null || !StrUtil.equals(cacheCode, code.toLowerCase())) {
            return fail(CodeEnum.IMAGE_CODE_ERROR);
        }
        if (!StrUtil.isBlank(smsCode)) {
            final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
            if (webSiteConfig.getOpenCodeStatus()) {
                final String cacheSmsCode = (String) redisUtil.get(Constants.FORGET_PWD_SMS_CODE_PREFIX + userName);
                if (cacheSmsCode == null || !StrUtil.equals(cacheSmsCode, smsCode)) {
                    return fail(CodeEnum.SMS_CODE_ERROR);
                }
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("updateUserName", userName);
        params.put("password", SecureUtil.md5(password));
        return userService.update(params) ? success() : fail();
    }
}
