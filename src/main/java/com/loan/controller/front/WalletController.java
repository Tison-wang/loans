package com.loan.controller.front;

import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.Contract;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IContractService;
import com.loan.service.IUserService;
import com.loan.service.IWalletService;
import com.loan.service.IWithdrawService;
import com.loan.util.Constants;
import com.loan.util.ThreadLocalCache;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IUserService;
import com.loan.service.IWalletService;
import com.loan.service.IWithdrawService;
import com.loan.util.Constants;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author:
 * @time: 2019/12/7 22:43
 */
@Controller
@RequestMapping("/api/front/wallet")
@RequireLogin
public class WalletController extends BaseController {
    @Autowired
    private IWalletService walletService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IContractService contractService;
    @Autowired
    private IUserService userService;

    @RequestMapping("/index")
    @ResponseBody
    public JsonResult<Map<String, BigDecimal>> index() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        final Optional<Map<String, BigDecimal>> data = walletService.getAmountAndFreezeAmount(user.getId());
        return success(data.get());
    }

    /**
     * 提现tips
     *
     * @return
     */
    @RequestMapping(value = "/withdrawTips", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult withdrawTips() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("ownerId", user.getOwnerId());
        params.put("type", Constants.PAY_PAGE_TYPE);
        final Contract contract = contractService.getSingleContract(params);
        // fixme 报空
        return success(contract.getContent());
    }

    /**
     * 是否可以提现
     *
     * @return
     */
    @RequestMapping(value = "/canWithdraw", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult canWithdraw() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        final User selectUser = userService.getUserById(user.getId());
        if (selectUser.getAmount() == null || selectUser.getAmount().doubleValue() <= 0) {
            return fail(CodeEnum.NOT_ENOUGH_AMOUNT);
        }
        if (selectUser.getFreezeAmount() != null && selectUser.getFreezeAmount().doubleValue() > 0) {
            return fail(CodeEnum.HAS_FREEZE_AMOUNT);
        }
        final boolean result = withdrawService.hasUnProcessWithdrawRecord(user.getId());
        if (result) {
            return fail(CodeEnum.HAS_UN_PROCESS_WITHDRAW_RECORD);
        }
        return success();
    }

    /**
     * 提现
     *
     * @param amount 提现金额
     * @return
     */
    @RequestMapping(value = "/withdraw/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult withdraw(@PathVariable double amount) {
        if (amount <= 0) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "提现金额需大于0");
        }
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getAmount() == null || user.getAmount().doubleValue() <= 0) {
            return fail(CodeEnum.NOT_ENOUGH_AMOUNT);
        }
        if (user.getFreezeAmount() != null && user.getFreezeAmount().doubleValue() > 0) {
            return fail(CodeEnum.HAS_FREEZE_AMOUNT);
        }
        final boolean result = withdrawService.hasUnProcessWithdrawRecord(user.getId());
        if (result) {
            return fail(CodeEnum.HAS_UN_PROCESS_WITHDRAW_RECORD);
        }
        return withdrawService.withdraw(BigDecimal.valueOf(amount)) ? success() : fail();
    }
}
