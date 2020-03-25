package com.loan.controller.back;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.model.Faq;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IFaqService;
import com.loan.util.ThreadLocalCache;
import com.loan.model.Faq;
import com.loan.service.IFaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 常见问题管理
 * @author:
 * @time: 2019/11/29 0:02
 */
@RestController
@RequestMapping("/api/back/faq")
@RequireLogin
public class FaqManageController extends BaseController {

    @Autowired
    private IFaqService faqService;

    @PostMapping("/list")
    public JsonResult<PageInfo<Faq>> list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        if (user.getType() == 1) {
            params.put("userId", user.getId());
        }
        PageInfo<Faq> pageInfo = faqService.getFaqByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    @PostMapping("/add")
    public JsonResult add(Faq faq) {
        return faqService.addFaq(faq) ? success() : fail();
    }

    @PostMapping("/{id}/update")
    public JsonResult update(@PathVariable Integer id, Faq faq) {
        faq.setId(id);
        return faqService.updateFaq(BeanUtil.beanToMap(faq, false, true)) ? success() : fail();
    }


}
