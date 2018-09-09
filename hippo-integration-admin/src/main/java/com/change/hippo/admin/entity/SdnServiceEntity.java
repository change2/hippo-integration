package com.change.hippo.admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SdnServiceEntity {

    private Long id;
    private String serviceId;
    private String serviceDesc;
    private String serviceModel;
    private Boolean serviceLog;
    private String domain;
    private String path;
    private String param;
    private String mode;
    private String actionType;
    private Boolean permission;
    private String argument;
    private String interfaze;
    private String method;
    private int replenishRate;
    private int burstCapacity;
    private Boolean circuitBreakerEnabled;
    private int metricsRollingStatisticalWindowInMilliseconds;
    private int circuitBreakerSleepWindowInMilliseconds;
    private int circuitBreakerRequestVolumeThreshold;
    private int circuitBreakerErrorThresholdPercentage;
    private int executionIsolationSemaphoreMaxConcurrentRequests;
    private int fallbackIsolationSemaphoreMaxConcurrentRequests;
    private Boolean status;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public SdnServiceEntity() {

    }

    public SdnServiceEntity(SdnServiceEntity service) {
        this.id = service.id;
        this.createBy = service.createBy;
        this.createTime = new Date();
    }
}