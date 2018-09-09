package com.change.hippo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * User: change.long
 * Date: 2017/11/28
 * Time: 下午8:39
 */
public class JedisUtils2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUtils2.class);

    /**
     * 修改成获取jedis cluster对象，来检查redis nodes是否为空，
     * spring data 的clusterGetNodes抛出RuntimeException,不符合应用要求
     * @param impl
     * @param connectionFactory
     */
    public static void clusterNodes(Object impl, JedisConnectionFactory connectionFactory) {
        RedisConnection connection;
        connection = connectionFactory.getConnection();
        try {
            if (connection instanceof JedisClusterConnection) {
                JedisCluster jedisCluster = ((JedisClusterConnection) connection).getNativeConnection();
                Map<String, JedisPool> nodes = JedisUtils.getClusterNodes(jedisCluster);
                if (null == nodes || nodes.size() == 0) {
                    throw new InstantiationError("redis nodes is empty");
                }
                LOGGER.info("RedisTemplateFactory clazz={},cluster node={}", impl.getClass(), nodes);
            }
        } finally {
            if (connection != null) {
                RedisConnectionUtils.releaseConnection(connection, connectionFactory);
            }
        }
    }

}
