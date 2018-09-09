package com.change.hippo.admin.shiro;


import com.change.hippo.admin.utils.SpringContextUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    public static JedisCluster jedisCluster;

    static {
        jedisCluster = (JedisCluster) SpringContextUtils.getBeanById("jedisCluster");
    }


    public static boolean exists(String key) {
        try {
            return jedisCluster.exists(StringEscapeUtils.unescapeJava(key));
        } catch (Exception e) {
            logger.error(String.format("exists cache key:%s error", key), e);
            return false;
        }
    }

    public static Object get(String key) {
        try {
            return jedisCluster.get(StringEscapeUtils.unescapeJava(key));
        } catch (Exception e) {
            logger.error(String.format("get cache key:%s error", key), e);
            return null;
        }
    }
}
