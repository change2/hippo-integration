package com.change.hippo.utils.kafka;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class KafkaThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    public String threadName = KafkaThreadFactory.class.getSimpleName();

    public KafkaThreadFactory() {
        this("DefaultKafkaThread");
    }

    public KafkaThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable task) {
        return new Thread(task, threadName + "-" + threadNumber.getAndIncrement());
    }

}
