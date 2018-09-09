package org.change.hippo.server.service;

import org.apache.commons.beanutils.BeanUtils;
import org.change.hippo.server.model.ServiceConfig;

public abstract class AbstractService<T extends ServiceConfig> implements Service<T> {

    private String serviceId;

    private String actionType;

    private boolean permission;

    private String[] param;

    private boolean circuitBreakerEnabled;

    //FOR CIRCUIT BREAKER
    private int metricsRollingStatisticalWindowInMilliseconds;

    private int circuitBreakerRequestVolumeThreshold;

    private int circuitBreakerSleepWindowInMilliseconds;

    private int circuitBreakerErrorThresholdPercentage;

    private int executionIsolationSemaphoreMaxConcurrentRequests;

    private int fallbackIsolationSemaphoreMaxConcurrentRequests;

    //for redis limiter
    private int replenishRate;

    private int burstCapacity;

    private ServiceConfig serviceConfig;

    public AbstractService() {
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public boolean isPermission() {
        return permission;
    }

    @Override
    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public void postConstruct(ServiceConfig config) {
        try {
            BeanUtils.copyProperties(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCircuitBreakerEnabled() {
        return circuitBreakerEnabled;
    }

    public void setCircuitBreakerEnabled(boolean circuitBreakerEnabled) {
        this.circuitBreakerEnabled = circuitBreakerEnabled;
    }

    @Override
    public int getMetricsRollingStatisticalWindowInMilliseconds() {
        return metricsRollingStatisticalWindowInMilliseconds;
    }

    public void setMetricsRollingStatisticalWindowInMilliseconds(int metricsRollingStatisticalWindowInMilliseconds) {
        this.metricsRollingStatisticalWindowInMilliseconds = metricsRollingStatisticalWindowInMilliseconds;
    }

    @Override
    public int getCircuitBreakerRequestVolumeThreshold() {
        return circuitBreakerRequestVolumeThreshold;
    }

    public void setCircuitBreakerRequestVolumeThreshold(int circuitBreakerRequestVolumeThreshold) {
        this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
    }

    @Override
    public int getCircuitBreakerSleepWindowInMilliseconds() {
        return circuitBreakerSleepWindowInMilliseconds;
    }

    public void setCircuitBreakerSleepWindowInMilliseconds(int circuitBreakerSleepWindowInMilliseconds) {
        this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
    }

    @Override
    public int getCircuitBreakerErrorThresholdPercentage() {
        return circuitBreakerErrorThresholdPercentage;
    }

    public void setCircuitBreakerErrorThresholdPercentage(int circuitBreakerErrorThresholdPercentage) {
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
    }

    @Override
    public int getExecutionIsolationSemaphoreMaxConcurrentRequests() {
        return executionIsolationSemaphoreMaxConcurrentRequests;
    }

    public void setExecutionIsolationSemaphoreMaxConcurrentRequests(int executionIsolationSemaphoreMaxConcurrentRequests) {
        this.executionIsolationSemaphoreMaxConcurrentRequests = executionIsolationSemaphoreMaxConcurrentRequests;
    }

    @Override
    public int getFallbackIsolationSemaphoreMaxConcurrentRequests() {
        return fallbackIsolationSemaphoreMaxConcurrentRequests;
    }

    public void setFallbackIsolationSemaphoreMaxConcurrentRequests(int fallbackIsolationSemaphoreMaxConcurrentRequests) {
        this.fallbackIsolationSemaphoreMaxConcurrentRequests = fallbackIsolationSemaphoreMaxConcurrentRequests;
    }

    @Override
    public int getReplenishRate() {
        return replenishRate;
    }

    public void setReplenishRate(int replenishRate) {
        this.replenishRate = replenishRate;
    }

    @Override
    public int getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(int burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    @Override
    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
