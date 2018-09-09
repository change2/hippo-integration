package com.change.hippo.utils.kafka;

/**
 * 消息消费接口
 */
public interface MessageConsumer<T>{

    void consume(String payload);

}