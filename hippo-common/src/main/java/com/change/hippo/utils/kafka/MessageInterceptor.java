package com.change.hippo.utils.kafka;


import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MessageInterceptor implements ProducerInterceptor {


    private static final Logger LOGGER = LoggerFactory.getLogger(MessageInterceptor.class);

    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            LOGGER.error("Received an exception when sending kafka message shared metadata ={} ", metadata, exception);
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("send message successfully,share metadata={}", metadata);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
