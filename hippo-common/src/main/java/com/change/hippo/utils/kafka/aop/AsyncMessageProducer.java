package com.change.hippo.utils.kafka.aop;


import com.change.hippo.utils.kafka.Message;
import com.change.hippo.utils.kafka.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncMessageProducer implements MessageProducer {

    private MessageTransactionManager messageTransactionManager;

    private MessageProducer messageProducer;
    private Logger logger = LoggerFactory.getLogger(AsyncMessageProducer.class);

    public void setMessageProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    public void setMessageTransactionManager(MessageTransactionManager messageTransactionManager) {
        this.messageTransactionManager = messageTransactionManager;
    }

    @Override
    public void send(final String topic, final Message<?> message) {
        if (messageTransactionManager == null || messageTransactionManager.getMessageTransaction() == null) {
            messageProducer.send(topic, message);
            logger.debug("Send message to kafka end!");
            return;
        }
        messageTransactionManager.getMessageTransaction().execute(new MessageCarrier(messageProducer, message, topic));
        logger.debug("Submit message task end!");
    }

    @Override
    public void send(final String topic, final String key, final Message<?> message) {
        if (messageTransactionManager == null || messageTransactionManager.getMessageTransaction() == null) {
            messageProducer.send(topic, key, message);
            logger.debug("Send message to kafka end!");
            return;
        }
        messageTransactionManager.getMessageTransaction().execute(new MessageCarrier(messageProducer, message, topic, key));
        logger.debug("Submit message task end!");

    }

    @Override
    public boolean syncSend(final String topic, final String key, final Message<?> message) {
        if (messageTransactionManager == null || messageTransactionManager.getMessageTransaction() == null) {
            logger.debug("Send message to kafka end!");
            return messageProducer.syncSend(topic, key, message);
        }

        messageTransactionManager.getMessageTransaction().execute(new MessageCarrier(messageProducer, message, topic, key));
        logger.debug("Submit message task end!");
        return true;
    }

    @Override
    public boolean syncSend(String topic, Message<?> message) {
        if (messageTransactionManager == null || messageTransactionManager.getMessageTransaction() == null) {
            logger.debug("Send message to kafka end!");
            return messageProducer.syncSend(topic, message);
        }

        messageTransactionManager.getMessageTransaction().execute(new MessageCarrier(messageProducer, message, topic, null));
        logger.debug("Submit message task end!");
        return true;
    }
}
