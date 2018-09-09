package com.change.hippo.utils.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.multithread.RunnableAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自动提交offset， 无事务支持。
 * 同一消息可能被消费多次， 也有可能丢消息
 */
public class DefaultBatchMessageDispatcher implements MessageDispatcher {

    public final static int POLICY_SKIP_ERROR_MESSAGE = 0;

    public final static int POLICY_SKIP_ERROR_PARTITION = 1;

    public final static int POLICY_SKIP_ERROR_BATCH = 2;

    private final static Logger logger = LoggerFactory.getLogger(DefaultBatchMessageDispatcher.class);

    private Map<String, MessageConsumer> consumers;

    private int errorHandlePolicy = POLICY_SKIP_ERROR_MESSAGE;

    private OrderedExecutor orderedExecutor;

    public DefaultBatchMessageDispatcher() {

    }

    public void doDispatch(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> messages) {
        doBatchDispatch(messages);
    }

    protected Map<TopicPartition, OffsetAndMetadata> doBatchDispatch(ConsumerRecords<String, String> messages) {
        Set<TopicPartition> partitions = messages.partitions();
        HashMap<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<TopicPartition, OffsetAndMetadata>();
        for (TopicPartition partition : partitions) {
            AtomicLong offsetHolder = new AtomicLong(-1);
            try {
                doPartitionDispatch(messages.records(partition), offsetHolder);
                if (offsetHolder.get() > -1) {
                    offsetMap.put(partition, new OffsetAndMetadata(offsetHolder.incrementAndGet()));
                }
            } catch (Exception e) {
                logger.warn("Consume record occurs exception, errorHandlePolicy={}", errorHandlePolicy, e);
                break;
            }
        }
        dumpPartitionOffsetInfo(offsetMap);
        return offsetMap;
    }

    protected void doPartitionDispatch(List<ConsumerRecord<String, String>> list, AtomicLong offsetHolder)
            throws Exception {
        for (ConsumerRecord<String, String> record : list) {
            try {
                doMessageDispatch(record, offsetHolder);
            } catch (Exception e) {
                logger.error("Consume record occurs exception:", e);
                if (errorHandlePolicy > 1) {
                    throw e;
                } else {
                    logger.warn("Consume record occurs exception, errorHandlePolicy={}", errorHandlePolicy, e);
                }
            } finally {
                RequestIdentityHolder.clear();
            }
        }
    }

    protected void doMessageDispatch(ConsumerRecord<String, String> record, AtomicLong offsetHolder) throws Exception {
        String payload = record.value();
        KafkaInvocationHandler.supportRequestIdentity(record.headers());
        if (StringUtils.isEmpty(payload)) {
            logger.warn("The message has no a valid payload, ignore it!");
            offsetHolder.set(record.offset());
            return;
        }
        try {
            Message m = JSON.parseObject(payload, Message.class);
            String eventType = m.getEventType();
            MessageConsumer messageConsumer = consumers.get(eventType);
            if (messageConsumer == null) {
                logger.info("No consumer for event type {}", eventType);
                offsetHolder.set(record.offset());
            } else {
                logger.info("Consuming kafka message {}", m.getMessageId());
                if (null != orderedExecutor) {
                    String key = KafkaInvocationHandler.key(record);
                    SafeRunnableAdapter task = new SafeRunnableAdapter() {
                        @Override
                        public void safeRun() {
                            try {
                                messageConsumer.consume(payload);
                            } catch (Exception e) {
                                if (errorHandlePolicy > 0) {
                                    throw e;
                                } else {
                                    offsetHolder.set(record.offset());
                                    logger.warn("Consume record occurs exception, errorHandlePolicy={}", errorHandlePolicy, e);
                                }
                            }
                        }
                    };
                    RunnableAdapter ri = new RunnableAdapter(task);
                    orderedExecutor.submitOrdered(key, ri);
                } else {
                    messageConsumer.consume(payload);
                }
                offsetHolder.set(record.offset());
            }
        } catch (Exception e) {
            if (errorHandlePolicy > 0) {
                throw e;
            } else {
                offsetHolder.set(record.offset());
                logger.warn("Consume record occurs exception, errorHandlePolicy={}", errorHandlePolicy, e);
            }
        }
    }

    public void dumpPartitionOffsetInfo(Map<TopicPartition, OffsetAndMetadata> offsets) {
        if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("Partition Offset Dump:");
            if (offsets.isEmpty()) {
                sb.append("No message is be consumed!");
            } else {
                for (TopicPartition partition : offsets.keySet()) {
                    sb.append("topic=").append(partition.topic()).append(", partition=")
                            .append(partition.partition()).append(", offset=").append(offsets.get(partition).offset());
                }
            }
            logger.info(sb.toString());
        }
    }

    public void setConsumers(Map<String, MessageConsumer> consumers) {
        this.consumers = consumers;
    }

    public void setErrorHandlePolicy(int errorHandlePolicy) {
        this.errorHandlePolicy = errorHandlePolicy;
    }

    public void setOrderedExecutor(OrderedExecutor orderedExecutor) {
        this.orderedExecutor = orderedExecutor;
    }
}
