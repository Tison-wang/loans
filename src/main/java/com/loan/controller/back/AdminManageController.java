package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.jsonFilter.JSON;
import com.loan.model.*;
import com.loan.service.IContractService;
import com.loan.service.IFaqService;
import com.loan.service.ISystemConfigService;
import com.loan.service.IUserService;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/26 22:33
 */
@Controller
@RequestMapping("/api/back/admin")
@RequireLogin
public class AdminManageController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private IFaqService faqService;
    @Autowired
    private IContractService contractService;

    @RequestMapping("/list")
    @JSON(type = User.class, include = "id,userName,createTime,lastLoginTime,state,type")
    public JsonResult<PageInfo<User>> list(String searchName, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("searchName", searchName);
        params.put("excludeType", 0);
        PageInfo<User> pageInfo = userService.getUserByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    @RequestMapping("/add")
    @ResponseBody
    public JsonResult add(String userName, String password, String confirmPassword, @RequestParam(defaultValue = "1") Integer state) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password) || StrUtil.isBlank(confirmPassword)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        if (!StrUtil.equals(password, confirmPassword)) {
            return fail(CodeEnum.PASSWORD_DIFFER);
        }
        User user1 = userService.getUserByUserName(userName);
        if (user1 != null) {
            return fail(CodeEnum.USER_NAME_HAS_EXIST);
        }
        User addUser = new User();
        addUser.setUserName(userName);
        addUser.setPassword(password);
        addUser.setState(state);
        addUser.setType(1);
        final boolean result = userService.addUser(addUser);
        Map<String, Object> updateUserParams = new HashMap<>();
        updateUserParams.put("id", addUser.getId());
        updateUserParams.put("addOwnerId", addUser.getId());
        userService.update(updateUserParams);
        if (result) {
            //fixme 优化 可异步操作
            //初始化借款设置
            //取超级管理员的系统配置信息
            final SystemConfig superAdminConfig = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setOwnerId(addUser.getId());
            systemConfig.setMinLoan(superAdminConfig.getMinLoan());
            systemConfig.setMaxLoan(superAdminConfig.getMaxLoan());
            systemConfig.setDefaultLoan(superAdminConfig.getDefaultLoan());
            systemConfig.setAllowLoanMonths(superAdminConfig.getAllowLoanMonths());
            systemConfig.setDefaultLoanMonth(superAdminConfig.getDefaultLoanMonth());
            systemConfig.setServiceChargeRate(superAdminConfig.getServiceChargeRate());
            systemConfig.setRepaymentDate(superAdminConfig.getRepaymentDate());
            systemConfig.setCloseStatus(true);
            systemConfigService.addSystemConfig(systemConfig);
            //初始化合同
            Map<String, Object> params = new HashMap<>();
            params.put("ownerId", user.getId());
            final List<Contract> contracts = contractService.getList(params);
            for (Contract contract : contracts) {
                contract.setId(null);
                contract.setCreateTime(null);
                contract.setOwnerId(addUser.getId());
                contractService.addContract(contract);
            }
            //初始化常见问题
            params.clear();
            params.put("userId", user.getId());
            final List<Faq> faqList = faqService.getFaqList(params);
            for (Faq faq : faqList) {
                faq.setUserId(addUser.getId());
                faq.setCreateTime(new Date());
            }
            faqService.batchInsert(faqList);
        }
        return result ? success() : fail();
    }

    @RequestMapping("/update/{id}")
    @ResponseBody
    public JsonResult update(@PathVariable Integer id, String userName, String password, String confirmPassword, @RequestParam(defaultValue = "1") Integer state) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        if (StrUtil.isBlank(userName)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        if (!StrUtil.equals(password, confirmPassword)) {
            return fail(CodeEnum.PASSWORD_DIFFER);
        }
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("userName", userName);
        fields.put("state", state);
        if (!StrUtil.isBlank(password)) {
            fields.put("password", SecureUtil.md5(password));
        }
        return userService.update(fields) ? success() : fail();
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("state", -1);
        final boolean result = userService.update(fields);
        if (result) {
            // 删除系统设置信息
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setOwnerId(id);
            systemConfig.setStatus(-1);
            systemConfigService.updateSystemConfig(systemConfig);

            // 删除常见问题
            Map<String, Object> params = new HashMap<>();
            params.put("userId", id);
            params.put("state", -1);
            faqService.updateFaq(params);

            // 删除合同
            params.clear();
            params.put("ownerId", id);
            params.put("status", -1);
            contractService.update(params);
        }
        return result ? success() : fail();
    }
}
