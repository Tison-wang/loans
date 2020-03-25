package com.loan.controller.back;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.enums.OrderStateEnum;
import com.loan.exception.BizException;
import com.loan.jsonFilter.JSON;
import com.loan.jsonFilter.JSONS;
import com.loan.model.*;
import com.loan.service.ILoanService;
import com.loan.service.IUserService;
import com.loan.service.IWalletService;
import com.loan.third.sms.SmsServiceAdapter;
import com.loan.third.sms.ThirdSmsService;
import com.loan.util.ThreadLocalCache;
import com.loan.vo.LoanInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:28
 */
@Controller
@RequestMapping("/api/back/loan")
@RequireLogin
public class LoanManageController extends BaseController {
    @Autowired
    private ILoanService loanService;
    @Autowired
    private IWalletService walletService;
    @Autowired
    private IUserService userService;
    @Autowired
    private SmsServiceAdapter smsServiceAdapter;

    /**
     * 借款列表
     *
     * @param searchName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    @JSONS({@JSON(type = User.class, include = "userName,detail"), @JSON(type = UserDetail.class, include = "name")})
    public JsonResult<PageInfo<LoanInfoVo>> list(String searchName, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchName", searchName);
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() == 1) {
            params.put("ownerId", user.getId());
        }
        PageInfo<LoanInfoVo> pageInfo = loanService.getLoanInfoVoByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    /**
     * 修改借款信息
     *
     * @param id
     * @param info
     * @return
     */
    @RequestMapping("/{id}/update")
    @ResponseBody
    public JsonResult update(@PathVariable Integer id, LoanInfo info) {
        if (info.getState() != null && info.getState() != -1) {
            if (StrUtil.isBlank(info.getStateDesc())) {
                return fail(CodeEnum.PARAMS_ILLEGAL, "状态描述信息不能为空！");
            }
            final OrderStateEnum orderStateEnum = OrderStateEnum.getByCode(info.getState());
            if (orderStateEnum == null) {
                return fail(CodeEnum.PARAMS_ILLEGAL);
            }
            final LoanInfo loanInfo = loanService.getSingleById(id);
            if (loanInfo == null) {
                return fail(CodeEnum.SOURCE_NOT_EXIST);
            }
            switch (orderStateEnum) {
                case OS_01:
                    break;
                case OS_11: // 放款成功
                    walletService.addAmount(loanInfo.getUserId(), loanInfo.getAmount());
                    break;
                case OS_15: // 冻结
                    //增加冻结金额
                    walletService.addFreezeAmount(loanInfo.getUserId(), loanInfo.getCurrentWithdrawAmount());
                    break;
                case OS_17: // 解冻成功
                    //减少冻结金额
                    walletService.subtractFreezeAmount(loanInfo.getUserId(), loanInfo.getCurrentWithdrawAmount());
                    break;
                default:
                    break;
            }
            //发送短信
            final User user = userService.getUserById(loanInfo.getUserId());
            final SystemConfig systemConfig = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
            String smsContent = String.format("【%s】%s", systemConfig.getSmsSign(), info.getStateDesc());
            smsServiceAdapter.setSmsService(new ThirdSmsService()).setSystemConfig(systemConfig).sendMessage(user.getUserName(), smsContent, false);
        }
        info.setId(id);
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() == 1) {
            info.setOwnerId(user.getId());
        }
        info.setModifier(user.getId());
        info.setModifyTime(new Date());
        boolean result = loanService.update(BeanUtil.beanToMap(info, false, true));
        return result ? success() : fail();
    }

    /**
     * 上传打款图
     *
     * @param id
     * @param file
     * @return
     */
    @RequestMapping("{id}/upload/transferVoucher")
    @ResponseBody
    public JsonResult uploadTransferVoucher(@PathVariable Integer id, @RequestParam(name = "file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return fail(CodeEnum.EMPTY_FILE);
        }
        if (!file.getOriginalFilename().contains(".")) {
            return fail(CodeEnum.FILE_FORMAT_ERROR);
        }
        final LoanInfo loanInfo = loanService.getSingleById(id);
        if (loanInfo == null) {
            return fail(CodeEnum.SOURCE_NOT_EXIST);
        }
        String filePath;
        try {
            filePath = uploadFile(file);
        } catch (IOException e) {
            logger.warn("【上传文件失败】", e);
            throw new BizException(CodeEnum.UPLOAD_FILE_FAIL);
        }
        if (!StrUtil.isBlank(filePath)) {
            return loanService.afterUploadTransferVoucherHandle(id, filePath) ? success() : fail();
        }
        return fail();
    }

    /**
     * 发送短信
     *
     * @param id
     * @param content
     * @return
     */
    @PostMapping("/{id}/sendSms")
    @ResponseBody
    public JsonResult sendSms(@PathVariable Integer id, String content) {
        if (StrUtil.isBlank(content)) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        final LoanInfo loanInfo = loanService.getSingleById(id);
        final User user = userService.getUserById(loanInfo.getUserId());
        final SystemConfig systemConfig = (SystemConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.SYSTEM_CONFIG);
        String smsContent = String.format("【%s】%s", systemConfig.getSmsSign(), content);
        final boolean result = smsServiceAdapter.setSmsService(new ThirdSmsService()).setSystemConfig(systemConfig).sendMessage(user.getUserName(), smsContent, false);
        return result ? success() : fail();
    }
}
