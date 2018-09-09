package org.change.hippo.server.service;

import org.change.hippo.server.model.ServiceConfig;

public interface Service<T extends ServiceConfig> {

    boolean init(T t) throws Exception;

    Object invoke(String args) throws Exception;

    String getServiceId();

    boolean isPermission();

    String getActionType();

    String[] getParam();

    ServiceConfig getServiceConfig();

    boolean isCircuitBreakerEnabled();

    //FOR CIRCUIT BREAKER
    int getMetricsRollingStatisticalWindowInMilliseconds();

    int getCircuitBreakerRequestVolumeThreshold();

    int getCircuitBreakerSleepWindowInMilliseconds();

    int getCircuitBreakerErrorThresholdPercentage();

    int getExecutionIsolationSemaphoreMaxConcurrentRequests();

    int getFallbackIsolationSemaphoreMaxConcurrentRequests();


    //for redis limiter
    /**
     * 令牌桶上限。
     */
    int getReplenishRate();

    /**
     * 令牌桶填充平均速率，单位：秒。
     */
    int getBurstCapacity();
}
