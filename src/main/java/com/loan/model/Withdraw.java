package com.loan.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:21
 */
@Data
public class Withdraw extends BaseModel {

    private static final long serialVersionUID = -8324877074494342589L;
    private Integer userId;

    private BigDecimal amount;

    private Integer state;


}