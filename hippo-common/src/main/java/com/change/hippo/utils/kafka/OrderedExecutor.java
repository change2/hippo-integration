package com.change.hippo.utils.kafka;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Special executor which can order the tasks if a common key is given.
 * Runnables submitted with non-null key will guaranteed to run in order for the same key.
 */
public class OrderedExecutor {

    private final ListeningExecutorService threads[];
    private final long threadIds[];

    public OrderedExecutor(int numThreads) {
        this(numThreads, 0, Executors.defaultThreadFactory());
    }

    public OrderedExecutor(int numThreads, int capacity) {
        this(numThreads, capacity, Executors.defaultThreadFactory());
    }

    public OrderedExecutor(int numThreads, int capacity, int timeout) {
        this(numThreads, capacity, timeout, Executors.defaultThreadFactory());
    }

    public OrderedExecutor(int numThreads, int capacity, ThreadFactory threadFactory) {
        this("OrderedSafeExecutor", numThreads, capacity, 0, threadFactory);
    }

    public OrderedExecutor(int numThreads, int capacity, int timeout, ThreadFactory threadFactory) {
        this("OrderedSafeExecutor", numThreads, capacity, timeout, threadFactory);
    }

    private OrderedExecutor(String name, int numThreads, int capacity, int timeout, ThreadFactory threadFactory) {
        if (numThreads <= 0) {
            numThreads = Runtime.getRuntime().availableProcessors() * 2;
        }
        threads = new ListeningExecutorService[numThreads];
        threadIds = new long[numThreads];

        List<BlockingQueue<Runnable>> queueList = new ArrayList<BlockingQueue<Runnable>>(numThreads);

        for (int i = 0; i < numThreads; i++) {
            if (capacity < 0) {
                queueList.add(new LinkedBlockingQueue<Runnable>());
            } else if (capacity == 0) {
                queueList.add(new SynchronousQueue<Runnable>());
            } else {
                queueList.add(new ArrayBlockingQueue<Runnable>(capacity));
            }
        }

        for (int i = 0; i < numThreads; i++) {
            final ThreadPoolExecutor thread = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS, queueList.get(i),
                    new ThreadFactoryBuilder()
                            .setNameFormat(name + "-" + i)
                            .setThreadFactory(threadFactory)
                            .build(), new KafkaRejectedExecutionHandler(timeout));
            threads[i] = MoreExecutors.listeningDecorator(thread);
            final int idx = i;
            try {
                ListenableFuture<?> submit = threads[i].submit(new SafeRunnableAdapter() {
                    @Override
                    public void safeRun() {
                        long id = Thread.currentThread().getId();
                        threadIds[idx] = id;
                    }
                });
                submit.get();
            } catch (InterruptedException e) {
                throw new RuntimeException("Couldn't start thread " + i, e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Couldn't start thread " + i, e);
            }
        }
    }


    public ListeningExecutorService chooseThread(Object orderingKey) {
        if (threads.length == 1) {
            return threads[0];
        }
        int hash = KafkaInvocationHandler.signSafeMod(orderingKey.hashCode(), threads.length);
        return threads[hash];

    }

    /**
     * schedules a one time action to execute with an ordering guarantee on the key
     *
     * @param orderingKey
     * @param r
     */
    public void submitOrdered(Object orderingKey, Runnable r) {
        ListeningExecutorService listeningExecutorService = chooseThread(orderingKey);
        listeningExecutorService.submit(r);
    }

    /**
     * schedules a one time action to execute with an ordering guarantee on the key
     *
     * @param orderingKey
     * @param callable
     */
    public <T> ListenableFuture<T> submitOrdered(Object orderingKey, Callable<T> callable) {
        return chooseThread(orderingKey).submit(callable);
    }


    private long getThreadID(Object orderingKey) {
        // skip hashcode generation in this special case
        if (threadIds.length == 1) {
            return threadIds[0];
        }

        return threadIds[KafkaInvocationHandler.signSafeMod(orderingKey.hashCode(), threadIds.length)];
    }

    public void shutdown() {
        for (int i = 0; i < threads.length; i++) {
            threads[i].shutdown();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        boolean ret = true;
        for (int i = 0; i < threads.length; i++) {
            ret = ret && threads[i].awaitTermination(timeout, unit);
        }
        return ret;
    }

    /**
     * Force threads shutdown (cancel active requests) after specified delay,
     * to be used after shutdown() rejects new requests.
     */
    public void forceShutdown(long timeout, TimeUnit unit) {
        for (int i = 0; i < threads.length; i++) {
            try {
                if (!threads[i].awaitTermination(timeout, unit)) {
                    threads[i].shutdownNow();
                }
            } catch (InterruptedException exception) {
                threads[i].shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }


    public static void main(String[] args) {
        OrderedExecutor orderedExecutor = new OrderedExecutor(100, 100, 100);
        for (int j = 0, len1 = 10; j < len1; j++) {
            for (int i = 1000000000, len = 1000100000; i < len; i++) {
                int finalI = i;
                orderedExecutor.submitOrdered(i, new SafeRunnableAdapter() {
                    @Override
                    public void safeRun() {
                        System.out.printf("time=%s,key=%s,Thread=%s\n", System.currentTimeMillis(),finalI, Thread.currentThread().getName());
                    }
                });
            }
        }
    }
}