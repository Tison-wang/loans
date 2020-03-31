package com.loan.mq.rabbitmq;

import com.tsmq.api.dto.ObjectEntity;
import com.tsmq.api.utils.ObjectByteConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author
 * @version 1.0
 * @date 2020/3/29 22:00
 */
@Slf4j
@Component
public class RaMqQueueCustomer {

    @RabbitListener(queues = "amqp.queue.test2")
    public void process(Object hello) {
        ObjectEntity obj = ObjectByteConvert.toObject(((Message) hello).getBody());
        log.info("[rabbitmq消息已经消费-2]: {}", obj);
    }

}