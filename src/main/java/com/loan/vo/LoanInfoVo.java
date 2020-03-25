package com.loan.vo;

import com.loan.model.LoanInfo;
import com.loan.model.User;
import com.loan.model.LoanInfo;
import com.loan.model.User;
import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/11/28 19:33
 */
@Data
public class LoanInfoVo extends LoanInfo {
    private static final long serialVersionUID = 9045609533352035570L;
    /**
     * 用户信息
     */
    private User userInfo;
}
