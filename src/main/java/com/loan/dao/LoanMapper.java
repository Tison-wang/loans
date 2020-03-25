package com.loan.dao;

import com.loan.model.LoanInfo;
import com.loan.vo.LoanInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/27 23:05
 */
@Mapper
public interface LoanMapper {
    /**
     * 通过用户id获取用户的借款订单数
     *
     * @param userId
     * @return
     */
    int getCountByUserId(int userId);

    /**
     * 获取借款列表信息
     *
     * @param params
     * @return
     */
    List<LoanInfo> getLoanInfoList(Map<String, Object> params);

    /**
     * 获取借款列表信息
     *
     * @param params
     * @return
     */
    List<LoanInfoVo> getLoanInfoVoList(Map<String, Object> params);

    /**
     * 添加借款信息
     *
     * @param loanInfo
     * @return
     */
    int insert(LoanInfo loanInfo);

    /**
     * 更新借款信息
     *
     * @param fields
     * @return
     */
    int update(Map<String, Object> fields);

    /**
     * 获取单个借款信息
     *
     * @param params
     * @return
     */
    LoanInfo getSingleLoanInfo(Map<String, Object> params);
}
