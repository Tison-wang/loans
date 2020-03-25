package com.loan.util;

import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 22:35
 */
public final class JsonResultUtil {

    private JsonResultUtil() {
    }

    public static <T> JsonResult<T> success() {
        return new JsonResult<>(JsonResult.SUCCESS, null, "请求成功！");
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(JsonResult.SUCCESS, data, "请求成功！");
    }

    public static <T> JsonResult<T> fail() {
        return new JsonResult<>(JsonResult.FAIL, null, "操作失败！");
    }

    public static <T> JsonResult<T> fail(int code, String msg) {
        return new JsonResult<>(code, null, msg);
    }

    public static <T> JsonResult<T> fail(CodeEnum codeEnum) {
        if (codeEnum == null) {
            throw new IllegalArgumentException("codeEnum不能为空!");
        }
        return new JsonResult<>(codeEnum.getCode(), null, codeEnum.getMsg());
    }
}
