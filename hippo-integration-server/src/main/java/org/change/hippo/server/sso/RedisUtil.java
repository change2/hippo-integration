package org.change.hippo.server.sso;


import org.apache.commons.lang.StringEscapeUtils;
import org.change.hippo.server.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.JedisCluster;

import java.util.List;

public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static JedisCluster jedisCluster;

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

    public static <T> T eval(DefaultRedisScript redisScript, List<String> keys, List<String> scriptArgs) {
        T result;
        try {
            result = (T) jedisCluster.evalsha(redisScript.getSha1(), keys, scriptArgs);
        } catch (Exception e) {
            try {
                result = (T) jedisCluster.eval(redisScript.getScriptAsString(), keys, scriptArgs);
            } catch (Exception e1) {
                return null;
            }
        }
        return result;
    }
}
