package com.loan.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loan.dao.WithdrawMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.LoanInfo;
import com.loan.model.User;
import com.loan.model.Withdraw;
import com.loan.service.ILoanService;
import com.loan.service.IWalletService;
import com.loan.service.IWithdrawService;
import com.loan.util.ThreadLocalCache;
import com.loan.exception.BizException;
import com.loan.service.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @description:
 * @author:
 * @time: 2019/12/5 0:34
 */
@Service
public class WithdrawServiceImpl implements IWithdrawService {
    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private IWalletService walletService;
    @Autowired
    private ILoanService loanService;

    @Override
    public boolean save(Withdraw withdraw) {
        if (withdraw == null || withdraw.getUserId() == null) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        withdraw.setCreateTime(new Date());
        withdraw.setState(0);
        return withdrawMapper.insert(withdraw) > 0;
    }

    @Override
    public boolean withdraw(BigDecimal withdrawAmount) {
        if (withdrawAmount == null || withdrawAmount.doubleValue() <= 0) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user == null) {
            throw new BizException(CodeEnum.NO_LOGIN);
        }
        final Optional<BigDecimal> amount = walletService.getAmount(user.getId());
        if (!amount.isPresent() || amount.get().doubleValue() < withdrawAmount.doubleValue()) {
            throw new BizException(CodeEnum.NOT_ENOUGH_AMOUNT);
        }

        //获取未完成订单
        final Optional<List<LoanInfo>> optionalLoanInfos = loanService.getUndoneLoanList(user.getId());
        if (!optionalLoanInfos.isPresent()) {
            throw new BizException(CodeEnum.OPTION_FAIL);
        }
        //增加提现记录
        Withdraw withdraw = new Withdraw();
        withdraw.setUserId(user.getId());
        withdraw.setAmount(withdrawAmount);
        save(withdraw);

        final LoanInfo loanInfo = optionalLoanInfos.get().get(0);
        Map<String, Object> params = new HashMap<>();
        params.put("id", loanInfo.getId());
        //借款订单增加当前提现金额
        params.put("currentWithdrawAmount", withdrawAmount);
        //清空之前的打款凭证
        params.put("setTransferVoucherNull", true);
        return loanService.update(params);
    }

    @Override
    public PageInfo<Withdraw> getWithdrawByPage(int pageNum, int pageSize, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        PageHelper.startPage(pageNum, pageSize);
        final List<Withdraw> withdraws = withdrawMapper.selectList(params);
        return new PageInfo<>(withdraws);
    }

    @Override
    public boolean hasUnProcessWithdrawRecord(int userId) {
        if (userId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("state", 0);
        final List<Withdraw> withdraws = withdrawMapper.selectList(params);
        return !CollectionUtil.isEmpty(withdraws);
    }

    @Override
    public boolean update(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return withdrawMapper.update(params) > 0;
    }
}
