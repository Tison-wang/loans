package com.loan.model;

import lombok.Data;

/**
 * @description: 阿里云短信模板
 * @author:
 * @time: 2019/12/12 19:36
 */

@Data
public class AliyunSmsTemplate {
    /**
     * 订单状态
     */
    private int orderState;
    /**
     * 模板的code值
     */
    private String code;
}
