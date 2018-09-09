package org.change.hippo.server.selector;

import org.change.hippo.server.util.Constants;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;
import org.change.hippo.server.sso.RedisUtil;
import org.change.hippo.server.util.Messages;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * burstCapacity 令牌桶上限。
 * replenishRate ，令牌桶填充平均速率，单位：秒。
 */
public class CheckRateLimiterSelector implements MessageSelector {

    private static DefaultRedisScript<List> REQUEST_RATE_LIMITER = new DefaultRedisScript<List>();
    private ServicePool servicePool;

    static {
        ClassPathResource classPathResource = new ClassPathResource("META-INF/scripts/request_rate_limiter.lua");
        REQUEST_RATE_LIMITER.setScriptSource(new ResourceScriptSource(classPathResource));
        REQUEST_RATE_LIMITER.setResultType(List.class);
    }

    @Override
    public boolean accept(Message<?> message) {
        String serviceId = message.getHeaders().get(Constants.X_SERVICE_ID, String.class);
        Service<?> service = servicePool.getService(serviceId);
        int replenishRate = service.getReplenishRate();
        int burstCapacity = service.getBurstCapacity();
        if (replenishRate <= 0 || burstCapacity <= 0) {
            return true;
        }
        List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                Instant.now().getEpochSecond() + "", Constants.TOKEN_REQUESTED);
        List<Long> evalArray = RedisUtil.eval(REQUEST_RATE_LIMITER, getKeys(serviceId), scriptArgs);
        if (null == evalArray || evalArray.size() != 2) {
            return true;
        }
        boolean allowed = evalArray.get(0) == 1L;
        Long tokensLeft = evalArray.get(1);
        Messages.setResponseHeader(Constants.REMAINING_HEADER, tokensLeft.toString());
        Messages.setResponseHeader(Constants.REPLENISH_RATE_HEADER, String.valueOf(service.getReplenishRate()));
        Messages.setResponseHeader(Constants.BURST_CAPACITY_HEADER, String.valueOf(service.getBurstCapacity()));
        return allowed;
    }

    public static List<String> getKeys(String id) {
        String tokenKey = String.format("request_rate_limiter.{%s}.tokens", id);
        String timestampKey = String.format("request_rate_limiter.{%s}.timestamp", id);
        return Arrays.asList(tokenKey, timestampKey);
    }

    public void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }
}
