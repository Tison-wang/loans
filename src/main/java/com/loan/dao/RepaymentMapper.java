package com.loan.dao;

import com.loan.model.Repayment;
import com.loan.model.Repayment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/9 21:29
 */
@Mapper
public interface RepaymentMapper {

    int insert(Repayment repayment);

    Repayment selectSingle(Map<String, Object> params);

    List<Repayment> selectList(Map<String, Object> params);

    int update(Map<String, Object> params);
}
