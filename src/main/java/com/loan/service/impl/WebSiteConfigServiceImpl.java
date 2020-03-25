package com.loan.service.impl;

import com.loan.dao.WebSiteConfigMapper;
import com.loan.model.WebSiteConfig;
import com.loan.service.IWebSiteConfigService;
import com.loan.model.WebSiteConfig;
import com.loan.service.IWebSiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author:
 * @time: 2019/12/4 21:54
 */
@Service
public class WebSiteConfigServiceImpl implements IWebSiteConfigService {

    @Autowired
    private WebSiteConfigMapper webSiteConfigMapper;

    @Override
    public WebSiteConfig getWebSiteConfig() {
        return webSiteConfigMapper.select();
    }

    @Override
    public boolean update(WebSiteConfig config) {
        return webSiteConfigMapper.update(config) > 0;
    }
}
