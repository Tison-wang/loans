package com.loan.dao;

import com.loan.model.Faq;
import com.loan.model.Faq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/29 20:59
 */
@Mapper
public interface FaqMapper {
    /**
     * 添加faq
     *
     * @param faq
     * @return
     */
    int insert(Faq faq);

    /**
     * 更新faq
     *
     * @param params
     * @return
     */
    int update(Map<String, Object> params);

    /**
     * 获取faq列表
     *
     * @param params
     * @return
     */
    List<Faq> getFaqList(Map<String, Object> params);

    /**
     * 获取单个faq
     *
     * @param params
     * @return
     */
    Faq getSingleFaq(Map<String, Object> params);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int batchInsert(List<Faq> list);
}
