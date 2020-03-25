package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.jsonFilter.JSON;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.RedisUtil;
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
 * @time: 2019/11/21 21:24
 */
@Controller
@RequestMapping("/api/back")
public class BackLoginController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/login")
    @JSON(type = User.class, filter = "password,detail,amount,freezeAmount,lastLoginTime,modifier,modifyTime,createTime")
    public JsonResult<User> login(String userName, String password) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        final User user = userService.getUserByUserName(userName);
        if (user == null) {
            return fail(CodeEnum.ACCOUNT_NOT_EXIST, "请检查域名是否正确！");
        }
        if (user.getState() == 0) {
            return fail(CodeEnum.ACCOUNT_FORBIDDEN);
        }
        if (!StrUtil.equals(user.getPassword(), SecureUtil.md5(password))) {
            return fail(CodeEnum.LOGIN_FAIL);
        }
        if (user.getType() == 0) {
            return fail(CodeEnum.FORBIDDEN);
        }
        //更新最后登录时间
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

    @RequestMapping("/logout")
    @ResponseBody
    public JsonResult logout(HttpServletRequest request) {
        final String accessToken = request.getHeader("accessToken");
        if (!StrUtil.isBlank(accessToken)) {
            redisUtil.del(accessToken);
        }
        return success();
    }

}
