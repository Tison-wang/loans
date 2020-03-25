package com.loan;

import com.loan.util.Constants;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @description:
 * @author:
 * @time: 2019/12/4 20:33
 */
public class MainTest {

    public static void main(String[] args) {
        //计算每月还款费用
        final double repayAmount = new BigDecimal(3000).divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(repayAmount);
        //计算每月服务费用
        final double serviceCharge = (new BigDecimal(3000).multiply(BigDecimal.valueOf(0.7))).divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(serviceCharge);
        System.out.println(repayAmount + serviceCharge);

        System.out.println(UUID.randomUUID().toString());

        System.out.println("17712345678".matches(Constants.MOBILE_REG));
    }
}
