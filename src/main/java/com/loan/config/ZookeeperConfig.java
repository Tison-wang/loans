/*
package com.loan.config;

import com.loan.handler.WatcherApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

*/
/**
 * ZookeeperConfig配置类
 *
 * @author
 * @version 1.0
 * @date 2020/3/9 17:30
 *//*

@Slf4j
@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int timeout;

    private static ZooKeeper zooKeeper = null;

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() {

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            // 连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            // 可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    log.info("当前状态为：" + event.getState() + "\t通知类型为：" + event.getType() + "\t操作的节点路径：" + event.getPath());
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        // 循环监听
                        try {
                            new Executor(connectString, "/zk-watcher-1", new WatcherApi()).run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            countDownLatch.await();
            log.info("【初始化ZooKeeper连接状态....】={}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("【初始化ZooKeeper连接异常....】={}", e);
        }
        return zooKeeper;
    }

}
*/
