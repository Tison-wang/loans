package com.loan;

import com.loan.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author:
 * @time: 2019/11/5 14:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {

    private Logger log = LoggerFactory.getLogger(RedisTests.class);

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void set(){
        redisUtil.set("test", "test",10);
        log.info("测试往redis中添加键值对。。。。。");
    }

    @Test
    public void get(){
        redisUtil.get("test");
    }
}
