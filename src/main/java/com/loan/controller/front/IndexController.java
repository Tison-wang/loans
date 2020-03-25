package com.loan.controller.front;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.jsonFilter.JSON;
import com.loan.model.JsonResult;
import com.loan.model.LoanInfo;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.service.ILoanService;
import com.loan.service.ISystemConfigService;
import com.loan.util.ThreadLocalCache;
import com.loan.service.ILoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:24
 */
@Controller
@RequestMapping("/api/front")
public class IndexController extends BaseController {

    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private ILoanService loanService;

    @RequestMapping("/template")
    @ResponseBody
    public JsonResult<String> template() {
        final SystemConfig systemConfig = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        return success(systemConfig.getIndexTemplate());
    }

    @RequestMapping("/index")
    @JSON(type = SystemConfig.class, include = "siteName,siteTitle,siteKeyword,siteDesc,statisticalCode,recordInfo,indexTemplate,bottomContent,minLoan,maxLoan,defaultLoan,allowLoanMonths,defaultLoanMonth,serviceChargeRate,repaymentDate")
    public JsonResult<SystemConfig> index() {
        final SystemConfig config = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        return success(config);
    }

    @RequestMapping("/hasUndoneLoan")
    @ResponseBody
    @RequireLogin
    public JsonResult<Boolean> hasUndoneLoan() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        final Optional<List<LoanInfo>> optionalLoanInfos = loanService.getUndoneLoanList(user.getId());
        if (optionalLoanInfos.isPresent()) {
            if (CollectionUtil.isNotEmpty(optionalLoanInfos.get())) {
                return success(true);
            }
        }
        return success(false);
    }

    @RequestMapping("/loan")
    @ResponseBody
    @RequireLogin
    public JsonResult loan(LoanInfo loanInfo) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        SystemConfig config = systemConfigService.getSingleSystemConfigByOwnerId(user.getOwnerId());
        if (config.getMinLoan().compareTo(loanInfo.getAmount()) > 0) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "贷款金额不能小于最小贷款金额！");
        }
        if (config.getMaxLoan().compareTo(loanInfo.getAmount()) < 0) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "贷款金额不能大于最大贷款金额！");
        }
        final String[] months = config.getAllowLoanMonths().split(",");
        if (!Arrays.asList(months).contains(loanInfo.getLoanTerm().toString())) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "不支持当前借款期限！");
        }
        if (StrUtil.isBlank(loanInfo.getUseFor())) {
            return fail(CodeEnum.PARAMS_ILLEGAL, "请填写借款用途！");
        }
        final JsonResult<Boolean> result = hasUndoneLoan();
        if (result.getData()) {
            return fail(CodeEnum.OPTION_FAIL, "有未完成的借款订单！");
        }
        //计算每月还款费用
        final double repayAmount = loanInfo.getAmount().divide(new BigDecimal(loanInfo.getLoanTerm()), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //计算每月服务费用
        final double serviceCharge = (loanInfo.getAmount().multiply(BigDecimal.valueOf(config.getServiceChargeRate()))).divide(new BigDecimal(loanInfo.getLoanTerm()), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        loanInfo.setRepayAmountOfMonth(new BigDecimal(repayAmount + serviceCharge));
        //服务费率
        loanInfo.setServiceChargeRate(config.getServiceChargeRate());
        return loanService.add(loanInfo) ? success() : fail();
    }
}
