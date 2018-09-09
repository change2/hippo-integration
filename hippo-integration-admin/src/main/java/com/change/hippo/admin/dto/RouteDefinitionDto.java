package com.change.hippo.admin.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
@Data
public class RouteDefinitionDto implements Serializable {
    private static final long serialVersionUID = 3469792846466100054L;

    private String serviceId;

    private String serviceDesc;

    private String serviceModel;

    @JSONField(name = "domainPath")
    private String domain;

    private String path;

    private String param;

    private String authCode;

    private boolean permission;

    private String argument;
    @JSONField(name = "interfaces")
    private String interfaze;

    private String method;
    @JSONField(name = "replenishrate")
    private int replenishRate;
    @JSONField(name = "burstcapacity")
    private int burstCapacity;
    @JSONField(name = "breakerswitch")
    private boolean circuitBreakerEnabled;
    @JSONField(name = "breakerwindow")
    private int metricsRollingStatisticalWindowInMilliseconds;
    @JSONField(name = "breakersemiopenstate")
    private int circuitBreakerSleepWindowInMilliseconds;
    @JSONField(name = "breakeropenthreshold")
    private int circuitBreakerRequestVolumeThreshold;
    @JSONField(name = "breakererrorrate")
    private int circuitBreakerErrorThresholdPercentage;
    @JSONField(name = "maxconcurrency")
    private int executionIsolationSemaphoreMaxConcurrentRequests;
    @JSONField(name = "failureback")
    private int fallbackIsolationSemaphoreMaxConcurrentRequests;

}