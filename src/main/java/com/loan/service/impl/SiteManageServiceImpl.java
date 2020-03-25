package com.loan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loan.dao.SiteManagerMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.service.ISiteMangeService;
import com.loan.vo.SiteManageVo;
import com.loan.dao.SiteManagerMapper;
import com.loan.exception.BizException;
import com.loan.service.ISiteMangeService;
import com.loan.vo.SiteManageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/19 22:14
 */
@Service
public class SiteManageServiceImpl implements ISiteMangeService {
    @Autowired
    private SiteManagerMapper siteManagerMapper;

    @Override
    public PageInfo<SiteManageVo> getListByPage(int pageNum, int pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<SiteManageVo> list = siteManagerMapper.getList(params);
        return new PageInfo<>(list);
    }

    @Override
    public SiteManageVo getSingle(int systemConfigId) {
        if (systemConfigId < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("systemConfigId", systemConfigId);
        return siteManagerMapper.getList(params).get(0);
    }
}
