package com.change.hippo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * User: change.long
 * Date: 2017/11/28
 * Time: 下午8:39
 */
public class JedisUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUtils.class);

    public static JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPoolConfig.setMaxTotal(200);
        jedisPoolConfig.setMaxIdle(3);
        jedisPoolConfig.setMinIdle(1);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(false);
        jedisPoolConfig.setTestWhileIdle(false);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(120000);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(60000);
        jedisPoolConfig.setNumTestsPerEvictionRun(-1);
        return jedisPoolConfig;
    }

    public static void detectJedisCluster(JedisCluster jedisCluster) {
        Map<String, JedisPool> nodes = JedisUtils.getClusterNodes(jedisCluster);
        LOGGER.info("jedisCluster初始化成功，配置地址信息为={},集群信息={}", jedisCluster, nodes);
        if (nodes == null || nodes.isEmpty()) {
            throw new InstantiationError("redis nodes is empty");
        }
    }

    public static Map<String, JedisPool> getClusterNodes(JedisCluster jedisCluster) {
        return jedisCluster.getClusterNodes();
    }

}
