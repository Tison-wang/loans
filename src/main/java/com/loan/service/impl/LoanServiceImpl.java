package com.loan.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loan.dao.LoanMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.LoanInfo;
import com.loan.model.Repayment;
import com.loan.model.User;
import com.loan.service.ILoanService;
import com.loan.service.IRepaymentService;
import com.loan.service.IWalletService;
import com.loan.service.IWithdrawService;
import com.loan.util.Constants;
import com.loan.util.ThreadLocalCache;
import com.loan.vo.LoanInfoVo;
import com.loan.exception.BizException;
import com.loan.model.Repayment;
import com.loan.service.ILoanService;
import com.loan.service.IRepaymentService;
import com.loan.service.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * @description:
 * @author:
 * @time: 2019/11/27 23:05
 */
@Service
public class LoanServiceImpl implements ILoanService {

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private IRepaymentService repaymentService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IWalletService walletService;

    @Override
    public int getCountByUserId(int userId) {
        if (userId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return loanMapper.getCountByUserId(userId);
    }

    @Override
    public PageInfo<LoanInfoVo> getLoanInfoVoByPage(int pageNum, int pageSize, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<LoanInfoVo> loanInfoVoList = loanMapper.getLoanInfoVoList(params);
        return new PageInfo<>(loanInfoVoList);
    }

    @Override
    public PageInfo<LoanInfo> getLoanInfoByPage(int pageNum, int pageSize, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<LoanInfo> loanInfoList = loanMapper.getLoanInfoList(params);
        return new PageInfo<>(loanInfoList);
    }

    @Override
    public boolean update(Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return loanMapper.update(fields) > 0;
    }

    @Override
    public boolean add(LoanInfo info) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        info.setUserId(user.getId());
        info.setOwnerId(user.getOwnerId());
        info.setNo(Constants.ORDER_NO_PREFIX + Instant.now().toEpochMilli());
        info.setCreateTime(new Date());
        info.setState(0);
        return loanMapper.insert(info) > 0;
    }

    @Override
    public LoanInfo getSingleById(int id) {
        if (id < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return loanMapper.getSingleLoanInfo(params);
    }

    @Override
    public List<LoanInfo> getList(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return loanMapper.getLoanInfoList(params);
    }

    @Override
    public Optional<List<LoanInfo>> getUndoneLoanList(int userId) {
        if (userId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        //获取未完成订单
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("undone", true);
        return Optional.ofNullable(getList(params));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean afterUploadTransferVoucherHandle(int loanId, String transferVoucher) {
        if (loanId < 1 || StrUtil.isBlank(transferVoucher)) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        final LoanInfo loanInfo = getSingleById(loanId);
        if (loanInfo == null) {
            throw new BizException(CodeEnum.SOURCE_NOT_EXIST);
        }
        if (loanInfo.getCurrentWithdrawAmount() == null || loanInfo.getCurrentWithdrawAmount().doubleValue() <= 0) {
            throw new BizException(CodeEnum.NOT_EXIST_WITHDRAW_RECORD);
        }
        Map<String, Object> loanParams = new HashMap<>();
        loanParams.put("id", loanId);
        loanParams.put("transferVoucher", transferVoucher);
        //清空当前提现金额
        loanParams.put("currentWithdrawAmount", 0.00);
        //增加累计提现金额
        loanParams.put("addTotalWithdrawAmount", loanInfo.getCurrentWithdrawAmount());
        final boolean update = update(loanParams);
        if (update) {
            //扣除提现额度
            walletService.subtractAmount(loanInfo.getUserId(), loanInfo.getCurrentWithdrawAmount());
            //处理提现记录
            Map<String, Object> withdrawParams = new HashMap<>();
            withdrawParams.put("userId", loanInfo.getUserId());
            withdrawParams.put("state", 1);
            withdrawParams.put("updateState", 0);
            withdrawService.update(withdrawParams);
            //添加还款单
            Repayment repayment = new Repayment();
            repayment.setUserId(loanInfo.getUserId());
            repayment.setLoanId(loanInfo.getId());
            repayment.setLoanNo(loanInfo.getNo());

            //计算每月还款费用
            final double repayAmount = loanInfo.getCurrentWithdrawAmount().divide(new BigDecimal(loanInfo.getLoanTerm()), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //计算每月服务费用
            final double serviceCharge = (loanInfo.getCurrentWithdrawAmount().multiply(BigDecimal.valueOf(loanInfo.getServiceChargeRate()))).divide(new BigDecimal(loanInfo.getLoanTerm()), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            loanInfo.setRepayAmountOfMonth(new BigDecimal(repayAmount + serviceCharge));

            repayment.setAmount(loanInfo.getRepayAmountOfMonth());
            repayment.setTotalPeriod(loanInfo.getLoanTerm());
            return repaymentService.addRepayment(repayment);
        }
        return false;
    }
}
