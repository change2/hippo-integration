package com.change.hippo.utils.concurrent;

/**
 * @author change.long@gmail.com Date: 2017/10/18 Time: 下午4:04
 */

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class ConcurrentTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentTemplate.class);
    private final ExecutorService pool;
    private final CountDownLatch doneSignal;
    private int min = 0;
    private int max = 200000;
    private int tps = 10;

    public ConcurrentTemplate(int min, int max, int tps)
            throws Exception {
        this.min = min;
        this.max = max;
        this.tps = tps;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().
                setNameFormat("ConcurrentTemplate").build();
        this.pool = new ThreadPoolExecutor(tps, tps,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), threadFactory);
        this.doneSignal = new CountDownLatch(max - min);
    }

    public void start() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            for (int i = this.min; i < this.max; i++) {
                this.pool.execute(new Worker(doneSignal));
            }
            this.doneSignal.await();
            long l = System.currentTimeMillis() - startTime;
            LOGGER.info("total concurrentTest useTime={}", l);
        } finally {
            this.pool.shutdown();
        }
    }

    /**
     * 并发执行某件事情
     */
    public abstract void call();

    private class Worker implements Runnable {
        private final CountDownLatch doneSignal;

        private Worker(CountDownLatch doneSignal) {
            this.doneSignal=doneSignal;
        }

        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();
                ConcurrentTemplate.this.call();
                long end = System.currentTimeMillis();
                long l = end - start;
                ConcurrentTemplate.LOGGER.info("Worker Used Time={}", l);
            } catch (Exception e) {
                ConcurrentTemplate.LOGGER.info(e.getMessage(), e);
            }finally {
                doneSignal.countDown();
            }
        }
    }
}
