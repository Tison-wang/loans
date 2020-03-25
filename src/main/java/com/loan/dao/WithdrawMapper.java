package com.loan.dao;

import com.loan.model.Withdraw;
import com.loan.model.Withdraw;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WithdrawMapper {

    int insert(Withdraw record);

    Withdraw selectSingle(Integer id);

    List<Withdraw> selectList(Map<String, Object> params);

    int update(Map<String, Object> params);
}