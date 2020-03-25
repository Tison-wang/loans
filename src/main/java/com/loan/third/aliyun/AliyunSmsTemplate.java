package com.loan.third.aliyun;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/12/4 20:55
 */
@Data

public class AliyunSmsTemplate {

    private static final String DEFAULT_TEMPLATE_CODE = "SMS_177150245";

    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板code
     */
    private String templateCode;
    /**
     * 短信模板变量对应的实际值，JSON格式
     */
    private String templateParam;

    public AliyunSmsTemplate(String signName, String templateParam) {
        this(signName, DEFAULT_TEMPLATE_CODE, templateParam);
    }

    public AliyunSmsTemplate(String signName, String templateCode, String templateParam) {
        this.signName = signName;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
    }
}
