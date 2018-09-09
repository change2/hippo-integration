package com.change.hippo.utils.kafka;

/**
 * 抑制消费消息时产生的异常
 */
public interface ConsumeExceptionHandler {

    public void suppress(Exception e) throws Exception;

}
