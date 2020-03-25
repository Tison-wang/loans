package com.loan.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 20:36
 */
@Data
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 所属人编号
     */
    private Integer ownerId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户类型：0：普通用户（默认） 1；管理员 2：超级管理员
     */
    private Integer type;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 关联，用户详情信息
     */
    private UserDetail detail;
    /**
     * 可提现金额
     */
    private BigDecimal amount;
    /**
     * 冻结金额
     */
    private BigDecimal freezeAmount;

}
