package com.change.hippo.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * 消息分发接口
 * 将消息分发到应用层的消费者
 *
 * @return 每个分区消费情况
 */
public interface MessageDispatcher {

    public void doDispatch(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> messages);

}