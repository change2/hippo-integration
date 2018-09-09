package com.change.hippo.maintain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RouteDefinition implements Serializable {
    private static final long serialVersionUID = 3469792846466100054L;
    private String serviceId;
    private String serviceModel;
    private String serviceDesc;
    private Boolean serviceLog;
    private String domain;
    private String path;
    private String param;
    private String actionType;
    private boolean permission;
    private String argument;
    private String interfaze;
    private String method;
    private int replenishRate;
    private int burstCapacity;
    private boolean circuitBreakerEnabled;
    private int metricsRollingStatisticalWindowInMilliseconds;
    private int circuitBreakerSleepWindowInMilliseconds;
    private int circuitBreakerRequestVolumeThreshold;
    private int circuitBreakerErrorThresholdPercentage;
    private int executionIsolationSemaphoreMaxConcurrentRequests;
    private int fallbackIsolationSemaphoreMaxConcurrentRequests;
}
