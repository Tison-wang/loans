package com.loan.controller.front;

import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.model.Faq;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IFaqService;
import com.loan.util.ThreadLocalCache;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.model.Faq;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.service.IFaqService;
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
 * @time: 2019/11/30 19:47
 */
@Controller
@RequestMapping("/api/front/faq")
public class FrontFaqController extends BaseController {

    @Autowired
    private IFaqService faqService;

    @RequestMapping("/list")
    @ResponseBody
    @RequireLogin
    public JsonResult<PageInfo<Faq>> list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getOwnerId());
        PageInfo<Faq> pageInfo = faqService.getFaqByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    @RequestMapping("/detail/{id}")
    @ResponseBody
    public JsonResult<Faq> detail(@PathVariable Integer id) {
        Faq faq = faqService.getFaqById(id);
        return success(faq);
    }
}
