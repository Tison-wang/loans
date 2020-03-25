package com.loan.service;

import com.github.pagehelper.PageInfo;
import com.loan.model.LoanInfo;
import com.loan.vo.LoanInfoVo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author:
 * @time: 2019/11/27 23:05
 */
public interface ILoanService {
    /**
     * 通过用户id获取用户的借款订单数
     *
     * @param userId
     * @return
     */
    int getCountByUserId(int userId);

    /**
     * 分页获取借款列表信息
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<LoanInfoVo> getLoanInfoVoByPage(int pageNum, int pageSize, Map<String, Object> params);

    /**
     * 分页获取
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<LoanInfo> getLoanInfoByPage(int pageNum, int pageSize, Map<String, Object> params);

    /**
     * 更新贷款信息
     *
     * @param fields
     * @return
     */
    boolean update(Map<String, Object> fields);

    /**
     * 添加贷款信息
     *
     * @param info
     * @return
     */
    boolean add(LoanInfo info);

    /**
     * 通过id获取借款详情
     *
     * @param id
     * @return
     */
    LoanInfo getSingleById(int id);

    /**
     * 获取订单列表
     *
     * @param params
     * @return
     */
    List<LoanInfo> getList(Map<String, Object> params);

    /**
     * 获取用户未完成的借款列表
     *
     * @param userId
     * @return
     */
    Optional<List<LoanInfo>> getUndoneLoanList(int userId);

    /**
     * 上传打款图之后的操作
     * 1.清空当前提现金额
     * 2.增加累计提现金额
     * 3.处理提现记录
     * 4.增加还款订单
     *
     * @param loanId          借款编号
     * @param transferVoucher 打款凭证
     * @return
     */
    boolean afterUploadTransferVoucherHandle(int loanId, String transferVoucher);
}
