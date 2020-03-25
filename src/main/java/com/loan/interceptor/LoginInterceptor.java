package com.loan.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.loan.annotation.RequireLogin;
import com.loan.enums.CodeEnum;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.JsonResultUtil;
import com.loan.util.RedisUtil;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 21:25
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前被拦截的方法是否含有注解
        boolean assignableFromMethod = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (assignableFromMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取类上的注解
            RequireLogin annotation = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
            if (annotation == null) {
                //获取当前方法的自定义注解拦截器的注解
                annotation = handlerMethod.getMethodAnnotation(RequireLogin.class);
            }
            if (annotation != null) {
                final String accessToken = request.getHeader("accessToken");
                if (StrUtil.isBlank(accessToken)) {
                    response(response, CodeEnum.NO_LOGIN);
                    return false;
                }
                final Integer userId = (Integer) redisUtil.get(accessToken);
                if (userId == null) {
                    response(response, CodeEnum.NO_LOGIN);
                    return false;
                }
                final User user = userService.getUserById(userId);
                if (user == null) {
                    response(response, CodeEnum.NO_LOGIN);
                    return false;
                }
                if (user.getState() == -1) {
                    redisUtil.del(accessToken);
                    response(response, CodeEnum.ACCOUNT_NOT_EXIST);
                    return false;
                } else if (user.getState() == 0) {
                    redisUtil.del(accessToken);
                    response(response, CodeEnum.ACCOUNT_FORBIDDEN);
                    return false;
                }
                redisUtil.expire(accessToken, Constants.ACCESS_TOKEN_EXPIRE_TIME);
                ThreadLocalCache.put(ThreadLocalCache.KeyEnum.USER, user);
            }
        }
        return super.preHandle(request, response, handler);
    }

    private void response(HttpServletResponse response, CodeEnum codeEnum) throws IOException {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(JSONUtil.toJsonStr(JsonResultUtil.fail(codeEnum)));
    }
}
