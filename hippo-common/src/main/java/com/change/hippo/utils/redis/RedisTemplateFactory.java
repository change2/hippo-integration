package com.change.hippo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * User: change.long
 * Date: 2017/11/28
 * Time: 下午9:24
 */
public class RedisTemplateFactory implements FactoryBean<RedisTemplate> {
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplateFactory.class);
    private JedisPoolConfig jedisPoolConfig;
    private RedisSerializer keySerializer;
    private RedisSerializer valueSerializer;
    private String clusters;

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public RedisSerializer getKeySerializer() {
        return keySerializer;
    }

    @Override
    public RedisTemplate getObject() throws Exception {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        String[] args = this.clusters.split(";");
        RedisNode.RedisNodeBuilder redisNodeBuilder = new RedisNode.RedisNodeBuilder();
        for (String arg : args) {
            String[] address = arg.split(":");
            Integer port = Integer.valueOf(address[1]);
            config.addClusterNode(redisNodeBuilder.listeningAt(address[0], port).build());
        }
        if (jedisPoolConfig == null) {
            jedisPoolConfig = JedisUtils.jedisPoolConfig();
        }
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(config, jedisPoolConfig);
        connectionFactory.afterPropertiesSet();
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        if (keySerializer == null) {
            keySerializer = new StringRedisSerializer();
        }
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        if (valueSerializer == null) {
            valueSerializer = new JdkSerializationRedisSerializer();
        }
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        JedisUtils2.clusterNodes(this, connectionFactory);
        return redisTemplate;
    }

    @Override
    public Class getObjectType() {
        return RedisTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }
}
