package com.loan.dao;

import com.loan.vo.SiteManageVo;
import com.loan.vo.SiteManageVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/19 21:54
 */
@Mapper
public interface SiteManagerMapper {

    List<SiteManageVo> getList(Map<String, Object> params);

}
