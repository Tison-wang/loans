package com.loan.controller.back;

import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.model.WebSiteConfig;
import com.loan.service.IWebSiteConfigService;
import com.loan.util.ThreadLocalCache;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.model.WebSiteConfig;
import com.loan.service.IWebSiteConfigService;
import com.loan.util.ThreadLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author:
 * @time: 2019/12/6 23:43
 */
@RestController
@RequestMapping("/api/back/websiteConfig")
@RequireLogin
public class WebSiteConfigController extends BaseController {
    @Autowired
    private IWebSiteConfigService webSiteConfigService;

    @GetMapping("/view")
    public JsonResult<WebSiteConfig> view() {
        final WebSiteConfig webSiteConfig = (WebSiteConfig) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.WEBSITE_CONFIG);
        return success(webSiteConfig);
    }

    @PostMapping("/update")
    public JsonResult update(WebSiteConfig webSiteConfig) {
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() != 2) {
            return fail(CodeEnum.FORBIDDEN);
        }
        return webSiteConfigService.update(webSiteConfig) ? success() : fail();
    }
}
