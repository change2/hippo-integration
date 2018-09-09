package org.change.hippo.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ServiceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serviceId;
    /**
     * true需要验证登录
     * false 不需要验证登录
     */
    private boolean permission = true;

    /**
     * 是否检查权限值
     */
    private String actionType;

    /**
     * 接口参数
     */
    private String[] param;

    /**
     * 熔断开关.true开启，false关闭
     */
    private boolean circuitBreakerEnabled;

    //FOR CIRCUIT BREAKER
    private int metricsRollingStatisticalWindowInMilliseconds;

    private int circuitBreakerSleepWindowInMilliseconds;

    private int circuitBreakerRequestVolumeThreshold;

    private int circuitBreakerErrorThresholdPercentage;

    private int executionIsolationSemaphoreMaxConcurrentRequests;

    private int fallbackIsolationSemaphoreMaxConcurrentRequests;

    //for redis limiter
    private int replenishRate;
    private int burstCapacity;
}
