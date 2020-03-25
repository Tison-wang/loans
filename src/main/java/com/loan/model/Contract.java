package com.loan.model;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/12/1 18:55
 */

@Data
public class Contract extends BaseModel {

    private static final long serialVersionUID = 3930704627989145587L;
    /**
     * 所属人
     */
    private Integer ownerId;

    /**
     * 分类
     */
    private Integer type;

    /**
     * 分类名称
     */
    private String typeName;
    /**
     * 内容
     */
    private String content;
}
