package com.loan.service.impl;

import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.service.IWalletService;
import com.loan.exception.BizException;
import com.loan.model.User;
import com.loan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author:
 * @time: 2019/12/8 19:07
 */
@Service
public class WalletServiceImpl implements IWalletService {
    @Autowired
    private IUserService userService;

    @Override
    public Optional<Map<String, BigDecimal>> getAmountAndFreezeAmount(int userId) {
        if (userId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        final User user = userService.getUserById(userId);
        if (user == null) {
            return Optional.empty();
        }
        Map<String, BigDecimal> data = new HashMap<>();
        data.put("amount", user.getAmount());
        data.put("freezeAmount", user.getFreezeAmount());
        return Optional.of(data);
    }

    @Override
    public boolean subtractAmountAndAddFreezeAmount(int userId, BigDecimal amount, BigDecimal freezeAmount) {
        if (userId < 1 || (amount == null || amount.doubleValue() <= 0) || (freezeAmount == null || freezeAmount.doubleValue() <= 0)) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("subtractAmount", amount);
        params.put("addFreezeAmount", amount);
        return userService.update(params);
    }

    @Override
    public boolean addAmount(int userId, BigDecimal amount) {
        if (userId < 1 || amount == null || amount.doubleValue() <= 0) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("addAmount", amount);
        return userService.update(params);
    }

    @Override
    public boolean subtractAmount(int userId, BigDecimal amount) {
        if (userId < 1 || amount == null || amount.doubleValue() <= 0) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("subtractAmount", amount);
        return userService.update(params);
    }

    @Override
    public boolean addFreezeAmount(int userId, BigDecimal amount) {
        if (userId < 1 || amount == null || amount.doubleValue() <= 0) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("addFreezeAmount", amount);
        return userService.update(params);
    }

    @Override
    public boolean subtractFreezeAmount(int userId, BigDecimal amount) {
        if (userId < 1 || amount == null || amount.doubleValue() <= 0) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("subtractFreezeAmount", amount);
        return userService.update(params);
    }

    @Override
    public Optional<BigDecimal> getAmount(int userId) {
        if (userId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        final User user = userService.getUserById(userId);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(user.getAmount());
    }
}
