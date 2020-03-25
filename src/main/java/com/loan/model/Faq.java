package com.loan.model;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/11/29 20:38
 */
@Data
public class Faq extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 所属人编号
     */
    private Integer userId;
    /**
     * 名称
     */
    private String name;
    /**
     * 内容
     */
    private String content;
}
