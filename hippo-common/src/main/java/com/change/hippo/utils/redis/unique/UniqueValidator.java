package com.change.hippo.utils.redis.unique;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

/**
 * 基于redis cluster实现分布式唯一锁
 */
public class UniqueValidator {
    private final static Logger log = LoggerFactory.getLogger(UniqueValidator.class);
    private final static String DEFAULT_VALUE = "";
    private JedisCluster jedisCluster;
    private String prefix;

    public boolean isUnique(String key, int seconds) {
        String str;
        try {
            str = jedisCluster.set(prefix + key, DEFAULT_VALUE, "NX", "EX", seconds);
        } catch (Exception e) {
            log.error("Check your redis server,{}", e.getMessage(), e);
            return false;
        }
        return "OK".equalsIgnoreCase(str);
    }

    public boolean isUnique(String key) {
        return isUnique(key, 1800);
    }

    public void remove(String key) {
        try {
            jedisCluster.del(prefix + key);
        } catch (Exception e) {
            log.warn("Check your redis server,{}", e.getMessage(), e);
        }
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
