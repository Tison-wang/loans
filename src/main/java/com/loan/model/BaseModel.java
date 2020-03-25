package com.loan.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 21:58
 */
@Data
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 3118812384000060012L;
    /**
     * 编号
     */
    private Integer id;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 修改人
     */
    private Integer modifier;
}
