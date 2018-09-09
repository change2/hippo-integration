package com.change.hippo.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 支持消息处理+提交offset放在一个事务里完成
 * 每一消息只成功消费一次
 */
public class TransactedBatchMessageDispatcher extends DefaultBatchMessageDispatcher {

    private final static Logger logger = LoggerFactory.getLogger(TransactedBatchMessageDispatcher.class);


    public TransactedBatchMessageDispatcher() {
        this.setErrorHandlePolicy(POLICY_SKIP_ERROR_PARTITION);
    }

    @Override
    @Transactional
    public void doDispatch(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> messages) {
        Map<TopicPartition, OffsetAndMetadata> offsetMap = this.doBatchDispatch(messages);
        if (!offsetMap.isEmpty()) {
            logger.info("Starting commit offsets ...");
            kafkaConsumer.commitSync(offsetMap);
            logger.info("Commit offsets successfully!");
        }
    }

}
