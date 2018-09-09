package com.change.hippo.utils.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.internals.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;

public class MessageProducerImpl implements MessageProducer, InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(MessageProducerImpl.class);

    private String defaultAppId;

    private String bootstrapServers;

    private Properties props;

    private Producer<String, String> kafkaProducer;

    private Map<String, String[]> eventTopics;

    public MessageProducerImpl() {
        this.props = getDefaultConfig();
    }

    public static Properties getDefaultConfig() {
        Properties props = new Properties();
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Kafka kafkaProducer is initializing ...");
        if (StringUtils.isNotEmpty(bootstrapServers)) {
            props.put("bootstrap.servers", bootstrapServers);
        }
        //KafkaProducer 是线程安全
        this.kafkaProducer = new KafkaProducer<String, String>(props);
        logger.info("Kafka kafkaProducer is initialized!");

    }


    @Override
    public void send(String topic, Message<?> message) {
        send(topic, null, message);
    }

    @Override
    public void send(String topic, String key, Message<?> message) {
        sendToKafka(topic, key, message);
    }

    @Override
    public boolean syncSend(String topic, String key, Message<?> message) {
        Future<RecordMetadata> f = this.sendToKafka(topic, key, message);
        try {
            RecordMetadata recordMetadata = f.get();
            if (logger.isDebugEnabled()) {
                logger.debug("Sending topics {} with message id {} ,meta {} successfully", topic, message.getMessageId(), recordMetadata);
            }
            return true;
        } catch (Exception e) {
            logger.warn("Received an exception when waiting server acks!", e);
            return false;
        }
    }

    @Override
    public boolean syncSend(String topic, Message<?> message) {
        return syncSend(topic, null, message);
    }

    protected Future<RecordMetadata> sendToKafka(String topic, String key, Message<?> message) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("topics must not be null");
        }
        org.apache.kafka.common.internals.Topic.validate(topic);
        if (Topic.hasCollisionChars(topic)) {
            throw new IllegalArgumentException("topics must not be contains  ('.') or underscore ('_'),example `kafka-action`");
        }
        if (StringUtils.isEmpty(message.getMessageId())) {
            message.setMessageId(UUID.randomUUID().toString());
        }
        if (StringUtils.isEmpty(message.getAppId())) {
            message.setAppId(defaultAppId);
        }
        if (message.getSendTime() == null) {
            message.setSendTime(new Date());
        }
        String json = JSON.toJSONStringWithDateFormat(message, "yyyy-MM-dd HH:mm:ss.SSS");
        if (logger.isDebugEnabled()) {
            logger.debug("Sending topics {} with key {} and message {}", topic, key, json);
        }
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, json);
        Header header = KafkaInvocationHandler.setRequestIdentity();
        if (header != null) {
            record.headers().add(header);
        }
        return kafkaProducer.send(record);
    }

    protected String[] findTopics(String eventType) {
        if (eventType == null) {
            logger.error("Message event type must not be null!");
            return null;
        }
        return eventTopics.get(eventType);
    }

    public void setDefaultAppId(String defaultAppId) {
        this.defaultAppId = defaultAppId;
    }

    public void setProps(Properties props) {
        this.props.putAll(props);
    }

    public void setEventTopics(Map<String, String[]> eventTopics) {
        this.eventTopics = eventTopics;
    }

    @Override
    public void destroy() throws Exception {
        if (this.kafkaProducer != null) {
            logger.info("Kafka producer is closing ...");
            kafkaProducer.flush();
            kafkaProducer.close();
            logger.info("Kafka producer is closed!");
        }

    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
}
