package com.change.hippo.utils.kafka.aop;


import com.change.hippo.utils.kafka.Message;
import com.change.hippo.utils.kafka.MessageProducer;
import org.springframework.util.Assert;

public class MessageCarrier {

    private MessageProducer messageProducer;
    private Message<?> message;
    private String topic;
    private String key;


    public MessageCarrier(MessageProducer messageProducer, Message<?> message, String topic) {
        this.messageProducer = messageProducer;
        this.message = message;
        this.topic = topic;
    }

    public MessageCarrier(MessageProducer messageProducer, Message<?> message, String topic, String key) {
        this.messageProducer = messageProducer;
        this.message = message;
        this.topic = topic;
        this.key = key;
    }

    public void send() {
        Assert.notNull(this.topic);
        Assert.notNull(this.message);
        Assert.notNull(this.messageProducer);
        messageProducer.send(topic, key, message);
    }

}
