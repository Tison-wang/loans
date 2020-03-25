package com.loan.service;

import com.github.pagehelper.PageInfo;
import com.loan.model.Faq;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/29 20:37
 */
public interface IFaqService {
    /**
     * 添加faq
     *
     * @param faq
     * @return
     */
    boolean addFaq(Faq faq);

    /**
     * 更新faq
     *
     * @param params
     * @return
     */
    boolean updateFaq(Map<String, Object> params);

    /**
     * 分页获取faq
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    PageInfo<Faq> getFaqByPage(int pageNum, int pageSize, Map<String, Object> params);

    /**
     * 获取faq列表
     *
     * @param params
     * @return
     */
    List<Faq> getFaqList(Map<String, Object> params);

    /**
     * 通过id获取faq
     *
     * @param id
     * @return
     */
    Faq getFaqById(Integer id);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    boolean batchInsert(List<Faq> list);
}
