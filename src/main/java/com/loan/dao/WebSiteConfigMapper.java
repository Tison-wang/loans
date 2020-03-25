package com.loan.dao;

import com.loan.model.WebSiteConfig;
import com.loan.model.WebSiteConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WebSiteConfigMapper {

    WebSiteConfig select();

    int update(WebSiteConfig record);
}