package com.change.hippo.utils.kafka;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KafkaRejectedExecutionHandler implements RejectedExecutionHandler {
	private int timeout;
	public KafkaRejectedExecutionHandler(int timeout){
		this.timeout = timeout;
	}

	@Override
	public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
		try {
            BlockingQueue<Runnable> queue = executor.getQueue();
            if (timeout > 0) {
                queue.offer(task, timeout, TimeUnit.MILLISECONDS);
            } else {
				queue.put(task);
			}
		} catch (Exception e) {
			throw new RuntimeException("Work queue is full,can't submit task after wait "+timeout+"ms",e);
		}
	}
}
