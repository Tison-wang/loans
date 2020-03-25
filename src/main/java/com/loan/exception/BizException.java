package com.loan.exception;

import com.loan.enums.CodeEnum;

/**
 * @description:
 * @author:
 * @time: 2019/11/25 20:24
 */
public class BizException extends RuntimeException {

    private Integer code;

    private String msg;

    public BizException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(CodeEnum codeEnum) {
        this(codeEnum.getCode(), codeEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
