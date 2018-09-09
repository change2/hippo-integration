package com.change.hippo.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.internals.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息拉取工作线程
 */
public class MessagePoller implements Runnable, InitializingBean, DisposableBean {

    private static AtomicInteger tid = new AtomicInteger(0);

    private final Logger logger = LoggerFactory.getLogger(MessagePoller.class);

    private final AtomicBoolean running = new AtomicBoolean(true);

    private String bootstrapServers;

    private String groupId;

    private String[] topics;

    private long pollTimeout = 0;

    private Properties props;

    private MessageDispatcher messageDispatcher;

    private String DEFAULT_AUTO_OFFSET_RESET_LATEST = "latest";

    private String offset = DEFAULT_AUTO_OFFSET_RESET_LATEST;

    private Executor taskExecutor;

    private int concurrency;

    public MessagePoller() {
        this.props = getDefaultConfig();
    }

    private static Properties getDefaultConfig() {
        Properties props = new Properties();
        props.put("auto.commit.interval.ms", "60000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    @Override
    public void run() {
        while (running.get()) {
            //kafkaConsumer非线程安全
            KafkaConsumer<String, String> kafkaConsumer = null;
            logger.info("Starting message poller ....");
            try {
                kafkaConsumer = new KafkaConsumer<String, String>(props);
                kafkaConsumer.subscribe(Arrays.asList(topics));
                int i = 0;
                while (running.get()) {
                    ConsumerRecords<String, String> messages = kafkaConsumer.poll(pollTimeout);
                    i++;
                    if (i % 1000 == 0) {
                        i = 0;
                        logger.info("Polled 1000 times");
                    }
                    int count = messages.count();
                    if (count == 0) {
                        continue;
                    }
                    logger.info("{} messages have been polled!", count);
                    if (taskExecutor == null) {
                        call(kafkaConsumer, messages);
                    } else if (taskExecutor != null) {
                        KafkaConsumer<String, String> finalKafkaConsumer = kafkaConsumer;
                        taskExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                call(finalKafkaConsumer, messages);
                            }
                        });
                    }
                }
            } catch (Throwable e) {
                logger.error("Process kafka message error", e);
            } finally {
                if (kafkaConsumer != null) {
                    logger.info("Closing message poller ...");
                    try {
                        kafkaConsumer.close();
                    } catch (Throwable e) {
                        logger.warn("Closing message poller occurs exception", e);
                    }
                }
            }
        }
        logger.info("Shutdown the message poller thread!");
    }

    private void call(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> messages) {
        try {
            messageDispatcher.doDispatch(kafkaConsumer, messages);
        } catch (Throwable e) {
            logger.error("Error occur in message dispatcher:", e.getMessage());
            throw e;
        }
    }

    public void setTopics(String[] topics) {
        logger.info("Set topic list to: {}", Arrays.toString(topics));
        this.topics = topics;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Shutdowning the message poller thread!");
        this.running.set(false);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (topics == null || topics.length == 0) {
            throw new IllegalArgumentException("topics must not be null");
        }
        for (int i = 0, len = topics.length; i < len; i++) {
            String topic = topics[i];
            org.apache.kafka.common.internals.Topic.validate(topic);
            if (Topic.hasCollisionChars(topic)) {
                throw new IllegalArgumentException("topics must not be contains  ('.') or underscore ('_'),example `kafka-action`");
            }
        }
        if (this.bootstrapServers != null) {
            props.put("bootstrap.servers", bootstrapServers);
        }
        if (this.groupId != null) {
            props.put("group.id", groupId);
        }

        if (messageDispatcher instanceof TransactedBatchMessageDispatcher) { //手动提交
            props.put("enable.auto.commit", "false");
        } else { //自动提交
            props.put("enable.auto.commit", "true");
        }
        if (this.offset != null) {
            props.put("auto.offset.reset", offset);
        }

        if (concurrency <= 0) {
            //默认分区数为3，等于消费线程数
            concurrency = 3;
        }
        for (int i = 0, len = concurrency; i < len; i++) {
            startPoller();
        }
    }

    private void startPoller() {
        Thread thread = new Thread(this);
        thread.setName("MessagePoller-" + tid.getAndIncrement());
        thread.start();
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setPollTimeout(long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public void setProps(Properties props) {
        this.props.putAll(props);
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }
}
