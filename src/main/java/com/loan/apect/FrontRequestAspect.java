package com.loan.apect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.util.IpUtil;
import com.loan.util.SpringBeanUtil;
import com.loan.util.ThreadLocalCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @description:
 * @author:
 * @time: 2019/12/1 21:28
 */
@Component
@Aspect
public class FrontRequestAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontRequestAspect.class);

    @Around(value = "execution(public com.loan.model.JsonResult com.junecs.loan.controller.front.*.*(..))")
    public JsonResult around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        final User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        final HttpServletRequest request = SpringBeanUtil.getRequest();
        StringBuilder sb = new StringBuilder();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            sb.append(paraName + "=" + request.getParameter(paraName) + ",");
        }
        if (!sb.toString().equals("")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        if (user != null) {
            LOGGER.info("UserId: [" + user.getId() + "], RequestUri: [" + request.getRequestURI() + "], RequestIp: [" + IpUtil.getIpAddr(request) + "], RequestData: [" + sb.toString() + "].");
        } else {
            LOGGER.info("RequestUri: [" + request.getRequestURI() + "], RequestIp: [" + IpUtil.getIpAddr(request) + "], RequestData: [" + sb.toString() + "].");
        }
        JsonResult jsonResult = (JsonResult) joinPoint.proceed();
        ThreadLocalCache.clear();
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info("responseData: [" + mapper.writeValueAsString(jsonResult) + "], cost: " + (System.currentTimeMillis() - startTime) + "ms.");
        return jsonResult;
    }


}

