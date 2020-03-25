package com.loan.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;


/**
 * Watcher监听
 *
 * @author
 * @version 1.0
 * @date 2020/3/9 17:35
 */
@Slf4j
public class WatcherApi implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        log.info("【[WatcherApi]监听状态】={}", event.getState());
        log.info("【[WatcherApi]监听路径为】={}", event.getPath());
        log.info("【[WatcherApi]监听的类型为】={}", event.getType()); //  三种监听类型： 创建，删除，更新
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType()) {
                log.info("--------[WatcherApi]自定义连接事件回调--------");
            }
            //创建节点
            if (Event.EventType.NodeCreated == event.getType()) {
                log.info("------[WatcherApi]自定义创建节点事件回调------");
            }
            //修改节点
            if (Event.EventType.NodeDataChanged == event.getType()) {
                log.info("------[WatcherApi]自定义修改节点事件回调------");
            }
            //删除节点
            if (Event.EventType.NodeDeleted == event.getType()) {
                log.info("------[WatcherApi]自定义删除节点事件回调------");
            }
        }
    }

}

