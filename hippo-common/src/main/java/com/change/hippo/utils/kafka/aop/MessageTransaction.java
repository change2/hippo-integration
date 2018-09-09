package com.change.hippo.utils.kafka.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.LinkedList;

public class MessageTransaction {

    private Logger logger = LoggerFactory.getLogger(MessageTransaction.class);

    private LinkedList<MessageCarrier> queue;

    public void begin() {
        Assert.isNull(queue);
        queue = new LinkedList<MessageCarrier>();
    }

    public void execute(MessageCarrier messageCarrier) {
        if (messageCarrier != null && this.queue != null) {
            queue.add(messageCarrier);
        }
    }

    public void commit() {
        if (this.queue == null || this.queue.size() == 0) {
            return;
        }
        for (MessageCarrier messageCarrier : queue) {
            messageCarrier.send();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Send message end!total={}", queue.size());
        }
        queue = null;

    }

    public void rollback() {
        this.queue = null;
    }
}
