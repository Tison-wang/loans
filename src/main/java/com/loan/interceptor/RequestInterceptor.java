package com.loan.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.loan.enums.CodeEnum;
import com.loan.model.SystemConfig;
import com.loan.model.WebSiteConfig;
import com.loan.service.ISystemConfigService;
import com.loan.service.IWebSiteConfigService;
import com.loan.util.Constants;
import com.loan.util.JsonResultUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.model.WebSiteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 前端访问拦截器
 * @author:
 * @time: 2019/11/21 21:25
 */
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private IWebSiteConfigService webSiteConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String domain = request.getHeader("domain");
        if (StrUtil.isBlank(domain)) {
            response(response, CodeEnum.PARAMS_ILLEGAL.getCode(), CodeEnum.PARAMS_ILLEGAL.getMsg());
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("domain", domain);
        SystemConfig config = systemConfigService.getSingleSystemConfig(params);
        if (config == null) {
            response(response, CodeEnum.SITE_NOT_EXIST.getCode(), CodeEnum.SITE_NOT_EXIST.getMsg());
            return false;
        }
        String servletPath = request.getServletPath();
        if (servletPath.startsWith(Constants.FRONT_URL_PREFIX)) { // 前端访问，判断站点是否关闭
            if (config.getCloseStatus()) {
                String msg = CodeEnum.SITE_HAS_CLOSED.getMsg();
                if (!StrUtil.isBlank(config.getClosedTips())) {
                    msg = config.getClosedTips();
                }
                response(response, CodeEnum.SITE_HAS_CLOSED.getCode(), msg);
                return false;
            }
        }
        final WebSiteConfig webSiteConfig = webSiteConfigService.getWebSiteConfig();
        ThreadLocalCache.put(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG, webSiteConfig);
        ThreadLocalCache.put(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG, config);
        return super.preHandle(request, response, handler);
    }

    private void response(HttpServletResponse response, int code, String msg) throws IOException {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(JSONUtil.toJsonStr(JsonResultUtil.fail(code, msg)));
    }
}
