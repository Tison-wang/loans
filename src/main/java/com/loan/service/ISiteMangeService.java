package com.loan.service;

import com.github.pagehelper.PageInfo;
import com.loan.vo.SiteManageVo;
import com.loan.vo.SiteManageVo;

import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/19 21:48
 */
public interface ISiteMangeService {

    PageInfo<SiteManageVo> getListByPage(int pageNum, int pageSize, Map<String, Object> params);

    SiteManageVo getSingle(int systemConfigId);
}
