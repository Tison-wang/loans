package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.SystemConfig;
import com.loan.model.User;
import com.loan.service.ISiteMangeService;
import com.loan.service.ISystemConfigService;
import com.loan.util.RandomUtil;
import com.loan.util.ThreadLocalCache;
import com.loan.vo.SiteManageVo;
import com.loan.service.ISiteMangeService;
import com.loan.vo.SiteManageVo;
import com.loan.vo.UploadImgDetailVo;
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
 * @time: 2019/12/19 21:42
 */
@Controller
@RequestMapping("/api/back/siteManage")
@RequireLogin
public class SiteManageController extends BaseController {
    @Autowired
    private ISiteMangeService siteMangeService;
    @Autowired
    private ISystemConfigService systemConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public JsonResult<PageInfo<SiteManageVo>> list(String searchKey, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("searchKey", searchKey);
        final PageInfo<SiteManageVo> pageInfo = siteMangeService.getListByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    @RequestMapping("/{systemConfigId}/detail")
    @ResponseBody
    public JsonResult<SiteManageVo> detail(@PathVariable Integer systemConfigId) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        return success(siteMangeService.getSingle(systemConfigId));
    }

    @RequestMapping("/{systemConfigId}/update")
    @ResponseBody
    public JsonResult<SiteManageVo> update(@PathVariable Integer systemConfigId, SiteManageVo siteManageVo) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        if (StrUtil.isBlank(siteManageVo.getSmsSign()) && !siteManageVo.getSiteClosedStatus()) {
            return fail(CodeEnum.OPTION_FAIL, "请先配置站点的短信签名！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("domain", siteManageVo.getDomain());
        final SystemConfig config = systemConfigService.getSingleSystemConfig(params);
        if (config != null && !config.getId().equals(systemConfigId)) {
            return fail(CodeEnum.SITE_HAS_EXISTED);
        }
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setId(systemConfigId);
        systemConfig.setSiteName(siteManageVo.getSiteName());
        systemConfig.setDomain(siteManageVo.getDomain());
        systemConfig.setSmsSign(siteManageVo.getSmsSign());
        systemConfig.setIndexTemplate(siteManageVo.getIndexTemplate());
        systemConfig.setCloseStatus(siteManageVo.getSiteClosedStatus());
        systemConfig.setTotalSmsCount(siteManageVo.getTotalSmsCount());
        systemConfigService.updateSystemConfig(systemConfig);
        return success();
    }

    @RequestMapping("/getUploadImgDetail")
    @ResponseBody
    public JsonResult<UploadImgDetailVo> getUploadImgDetail() {
        return success(UploadImgDetailVo.builder()
                .batchNumber(RandomUtil.generatorSixNumberByParam(10))
                .executeWay(RandomUtil.getTransferType())
                .transferCompany(RandomUtil.getCompanyName())
                .transferAccount(RandomUtil.getBankCard())
                .transferArea(RandomUtil.getArea())
                .receiptName(RandomUtil.getChineseName())
                .receiptBank(RandomUtil.getBank())
                .receiptBankAccount(RandomUtil.getBankCard())
                .currency(RandomUtil.getCurrency())
                .transferAmount(RandomUtil.getNum(1, 6) + "000")
                .transferDate("2020/02/26")
                .transferType(RandomUtil.getTransferType())
                .executeWay("实时到账")
                .status(RandomUtil.getStatus())
                .bankRemark("无")
                .dealResult(RandomUtil.getDealResult())
                .customRemark("无")
                .bankRemark("无")
                .build());
    }
}
