package com.loan.third.aliyun;

import lombok.Data;

/**
 * @description:
 * @author:
 * @time: 2019/12/4 20:58
 */
@Data

public class AliyunAccount {

    private String accessKeyId;

    private String accessSecret;

    public AliyunAccount(String accessKeyId, String accessSecret) {
        this.accessKeyId = accessKeyId;
        this.accessSecret = accessSecret;
    }
}
