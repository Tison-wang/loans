package com.loan.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loan.dao.FaqMapper;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.Faq;
import com.loan.model.User;
import com.loan.service.BaseService;
import com.loan.service.IFaqService;
import com.loan.util.ThreadLocalCache;
import com.loan.exception.BizException;
import com.loan.service.IFaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/29 20:55
 */
@Service
public class FaqServiceImpl extends BaseService implements IFaqService {

    @Autowired
    private FaqMapper faqMapper;

    @Override
    public boolean addFaq(Faq faq) {
        if (faq == null) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        faq.setUserId(user.getId());
        faq.setCreateTime(new Date());
        faq.setState(1);
        return faqMapper.insert(faq) > 0;
    }

    @Override
    public boolean updateFaq(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return faqMapper.update(params) > 0;
    }

    @Override
    public PageInfo<Faq> getFaqByPage(int pageNum, int pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<Faq> faqList = faqMapper.getFaqList(params);
        return new PageInfo<>(faqList);
    }

    @Override
    public List<Faq> getFaqList(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        return faqMapper.getFaqList(params);
    }

    @Override
    public Faq getFaqById(Integer id) {
        if (id == null || id < 1) {
            throw new BizException(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return faqMapper.getSingleFaq(params);
    }

    @Override
    public boolean batchInsert(List<Faq> list) {
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }
        return faqMapper.batchInsert(list) == list.size();
    }
}
