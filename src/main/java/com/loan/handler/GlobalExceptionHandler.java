package com.loan.handler;

import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.JsonResult;
import com.loan.util.JsonResultUtil;
import com.loan.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author:
 * @time: 2019/11/25 20:34
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult handle(HttpServletRequest request, Exception e, HttpServletResponse response) {
        JsonResult result = JsonResultUtil.fail(CodeEnum.SYSTEM_ERROR);
        //如果是业务逻辑异常，返回具体的错误码与提示信息
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            result.setCode(bizException.getCode());
            result.setMsg(bizException.getMsg());
        } else {
            //对系统级异常进行日志记录
            logger.error("系统异常:", e);
        }
        return result;
    }
}
