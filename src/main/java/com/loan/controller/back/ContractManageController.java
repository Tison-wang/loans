package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.Contract;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IContractService;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 合同管理
 * @author:
 * @time: 2019/11/28 23:59
 */
@Controller
@RequestMapping("/api/back/contract")
@RequireLogin
public class ContractManageController extends BaseController {

    @Autowired
    private IContractService contractService;

    @RequestMapping("/view/{type}")
    @ResponseBody
    public JsonResult<Contract> view(@PathVariable Integer type) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("ownerId", user.getId());
        final Contract contract = contractService.getSingleContract(params);
        return success(contract);
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonResult update(Contract contract) {
        if (StrUtil.isBlank(contract.getContent())) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("ownerId", user.getId());
        params.put("type", contract.getType());
        Contract oldContract = contractService.getSingleContract(params);
        if (oldContract != null) {
            Map<String, Object> updateParams = new HashMap<>();
            updateParams.put("id", oldContract.getId());
            updateParams.put("content", contract.getContent());
            return contractService.update(updateParams) ? success() : fail();
        } else {
            contract.setOwnerId(user.getId());
            return contractService.addContract(contract) ? success() : fail();
        }
    }

}
