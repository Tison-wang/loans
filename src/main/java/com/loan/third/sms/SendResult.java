package com.loan.third.sms;

import lombok.Data;

/**
 * @description: 短信接口返回信息封装
 * @author:
 * @time: 2019/12/15 18:46
 */
@Data
public class SendResult {
    private String ReturnStatus;

    private String Message;

    private double RemainPoint;

    private long TaskID;

    private int SuccessCounts;
}
