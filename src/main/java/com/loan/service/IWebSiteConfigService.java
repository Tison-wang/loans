package com.loan.service;

import com.loan.model.WebSiteConfig;
import com.loan.model.WebSiteConfig;

/**
 * @description:
 * @author:
 * @time: 2019/12/4 21:53
 */
public interface IWebSiteConfigService {

    WebSiteConfig getWebSiteConfig();

    boolean update(WebSiteConfig config);
}
