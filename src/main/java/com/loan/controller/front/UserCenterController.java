package com.loan.controller.front;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.*;
import com.loan.service.ILoanService;
import com.loan.service.IRepaymentService;
import com.loan.service.IUserDetailService;
import com.loan.service.IUserService;
import com.loan.util.Constants;
import com.loan.util.RedisUtil;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:25
 */
@Controller
@RequestMapping("/api/front/userCenter")
@RequireLogin
public class UserCenterController extends BaseController {

    @Autowired
    private ILoanService loanService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserDetailService userDetailService;
    @Autowired
    private IRepaymentService repaymentService;

    /**
     * 获取个人信息
     *
     * @return
     */
    @RequestMapping("/userDetail/view")
    @ResponseBody
    public JsonResult<UserDetail> viewUserDetail() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        final UserDetail userdetail = userDetailService.getUserDetailByUserId(user.getId());
        return success(userdetail);
    }

    /**
     * 借款列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/loan/list")
    @ResponseBody
    public JsonResult<PageInfo<LoanInfo>> loanList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        PageInfo<LoanInfo> pageInfo = loanService.getLoanInfoByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    /**
     * 借款详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/loan/{id}/detail")
    @ResponseBody
    public JsonResult<LoanInfo> loanDetail(@PathVariable Integer id) {
        final LoanInfo loanInfo = loanService.getSingleById(id);
        return success(loanInfo);
    }

    /**
     * 还款列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/repayment/list")
    @ResponseBody
    public JsonResult<PageInfo<Repayment>> repaymentList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getId());
        PageInfo<Repayment> pageInfo = repaymentService.getRepaymentByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    /**
     * 修改密码
     *
     * @param code
     * @param password
     * @return
     */
    @RequestMapping("/password/update")
    @ResponseBody
    public JsonResult updatePassword(String code, String password) {
        if (StrUtil.isBlank(code) || StrUtil.isBlank(password)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        if (!password.matches(Constants.PASSWORD_REG)) {
            return fail(CodeEnum.PASSWORD_FORMAT_ERROR);
        }
        String cacheCode = (String) redisUtil.get(Constants.IMAGE_CODE_PREFIX + code.toLowerCase());
        if (cacheCode == null || !StrUtil.equals(cacheCode, code.toLowerCase())) {
            return fail(CodeEnum.IMAGE_CODE_ERROR);
        }
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("password", SecureUtil.md5(password));

        return userService.update(params) ? success() : fail();
    }

    /**
     * 个人信息完善情况
     *
     * @return
     */
    @RequestMapping("/userDetail/status")
    @ResponseBody
    public JsonResult<Map<String, Boolean>> userDetailStatus() {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        UserDetail detail = user.getDetail();
        boolean identityStatus = false;
        boolean userInfoStatus = false;
        boolean bankInfoStatus = false;
        if (detail != null) {
            identityStatus = !StrUtil.isBlank(detail.getName()) && !StrUtil.isBlank(detail.getIdCardNumber()) &&
                    !StrUtil.isBlank(detail.getIdCardFrontImage()) && !StrUtil.isBlank(detail.getIdCardReverseImage()) &&
                    !StrUtil.isBlank(detail.getIdCardHoldImage());
            userInfoStatus = !StrUtil.isBlank(detail.getMonthlyIncome()) && !StrUtil.isBlank(detail.getCurrentAddress()) &&
                    !StrUtil.isBlank(detail.getLinkName1()) && !StrUtil.isBlank(detail.getMobile1()) &&
                    !StrUtil.isBlank(detail.getRelation1()) &&
                    !StrUtil.isBlank(detail.getLinkName2()) && !StrUtil.isBlank(detail.getMobile2()) &&
                    !StrUtil.isBlank(detail.getRelation2());
            bankInfoStatus = !StrUtil.isBlank(detail.getBankName()) && !StrUtil.isBlank(detail.getBankCardNumber());
        }
        Map<String, Boolean> result = new HashMap<>();
        result.put("identityStatus", identityStatus);
        result.put("userInfoStatus", userInfoStatus);
        result.put("bankInfoStatus", bankInfoStatus);
        return success(result);
    }

    /**
     * 完善个人信息
     *
     * @param userDetail
     * @return
     */
    @RequestMapping("/addUserInfo")
    @ResponseBody
    public JsonResult addUserInfo(UserDetail userDetail) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        boolean result;
        if (user.getDetail() != null) {
            userDetail.setId(user.getDetail().getId());
            result = userDetailService.update(userDetail);
        } else {
            userDetail.setUserId(user.getId());
            result = userDetailService.addUserDetail(userDetail);
        }
        if (result) {
            final UserDetail newUserDetail = userDetailService.getUserDetailByUserId(user.getId());
            user.setDetail(newUserDetail);
            ThreadLocalCache.put(ThreadLocalCache.KeyEnum.USER, user);
            return success();
        }
        return fail();
    }
}
