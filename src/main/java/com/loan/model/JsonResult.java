package com.loan.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/21 21:22
 */
@Data
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer SUCCESS = 200;

    public static final Integer FAIL = 500;

    private Integer code;

    private T data;

    private Map<String, Object> extendData;

    private String msg;

    public JsonResult(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
