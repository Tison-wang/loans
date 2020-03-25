package com.loan.model;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:02
 */
@Data
public class UserDetail extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;
    /**
     * 用户编号
     */
    private Integer userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号码
     */
    private String idCardNumber;
    /**
     * 身份证正面照
     */
    private String idCardFrontImage;
    /**
     * 身份证反面照
     */
    private String idCardReverseImage;
    /**
     * 手指身份证照
     */
    private String idCardHoldImage;
    /**
     * 现居住地址
     */
    private String currentAddress;
    /**
     * 月收入
     */
    private String monthlyIncome;
    //==================直系亲属联系人==========================
    /**
     * 姓名
     */
    private String linkName1;
    /**
     * 手机号
     */
    private String mobile1;
    /**
     * 关系
     */
    private String relation1;
    //==================其他联系人==========================
    /**
     * 姓名
     */
    private String linkName2;
    /**
     * 手机号
     */
    private String mobile2;
    /**
     * 关系
     */
    private String relation2;
    //======================银行卡信息==============================
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行卡号
     */
    private String bankCardNumber;
}
