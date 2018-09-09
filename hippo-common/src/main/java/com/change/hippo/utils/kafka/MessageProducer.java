package com.change.hippo.utils.kafka;

/**
 * 消息发送接口
 */
public interface MessageProducer {

    /**
     * 异步发送消息到指定topic
     *
     * @param topic
     * @param message
     */
    public void send(String topic, Message<?> message);

    /**
     * 异步发送消息到指定topic
     *
     * @param topic
     * @param key
     * @param message
     */
    public void send(String topic, String key, Message<?> message);

    /**
     * 同步发送消息
     *
     * @param topic 主题
     * @param key 消息的key，用作一直顺序消费使用
     * @param message 内容
     * @return true 发送成功， false 结果未知
     */
    public boolean syncSend(String topic, String key, Message<?> message);


    /**
     * 同步发送消息
     *
     * @param topic 主题
     * @param message 内容
     * @return true 发送成功， false 结果未知
     */
    public boolean syncSend(String topic, Message<?> message);

}
