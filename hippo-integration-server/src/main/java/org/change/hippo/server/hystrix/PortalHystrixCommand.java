package org.change.hippo.server.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import org.apache.commons.lang3.StringUtils;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.apache.commons.lang3.math.NumberUtils.max;

public class PortalHystrixCommand extends HystrixCommand<Object> {
    private static final Logger logger = LoggerFactory.getLogger(PortalHystrixCommand.class);
    private final String payload;
    private final Service<?> service;
    private static final String SEPARATOR = "-";


    public PortalHystrixCommand(Service<?> service, String payload) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(service.getServiceId()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(StringUtils.join(new Object[]{service.getServiceId(), Arrays.toString(service.getParam())}, SEPARATOR)))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withMetricsRollingStatisticalWindowInMilliseconds(max(service.getMetricsRollingStatisticalWindowInMilliseconds(), Constants.CIRCUIT_BREAKER_ROLLING_TIME))
                        .withCircuitBreakerRequestVolumeThreshold(max(service.getCircuitBreakerRequestVolumeThreshold(), Constants.CIRCUIT_BREAKER_REQUEST_VALUME))
                        .withCircuitBreakerSleepWindowInMilliseconds(max(service.getCircuitBreakerSleepWindowInMilliseconds(), Constants.CIRCUIT_BREAKER_SLEEP_TIME))
                        .withCircuitBreakerErrorThresholdPercentage(max(service.getCircuitBreakerErrorThresholdPercentage(), Constants.CIRCUIT_BREAKER_ERROR_PERCENT))
                        .withExecutionTimeoutEnabled(false)
                        .withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)  //使用信号量隔离
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(
                                max(service.getExecutionIsolationSemaphoreMaxConcurrentRequests(), Constants.CIRCUIT_BREAKER_MAX_CONCURRENT_REQUESTS))   //设置最大并发数
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(
                                max(service.getFallbackIsolationSemaphoreMaxConcurrentRequests(), Constants.CIRCUIT_BREAKER_MAX_CONCURRENT_REQUESTS)))); //设置失败回滚调用最大并发数
        this.service = service;
        this.payload = payload;
    }

    /**
     * 根据需要重写失败回退逻辑（降级逻辑）,返回null
     *
     * @return 返回熔断处理生成的对象
     */
    @Override
    protected Object getFallback() {
        if (logger.isWarnEnabled()) {
            logger.warn("CircuitBreak fallback,return=null");
        }
        return null;
    }


    @Override
    protected Object run() throws Exception {
        try {
            return service.invoke(payload);
        } catch (Exception e) {
            throw e;
        }
    }
}