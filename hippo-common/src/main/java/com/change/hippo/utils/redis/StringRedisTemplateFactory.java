package com.change.hippo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * User: change.long
 * Date: 2017/11/28
 * Time: 下午9:24
 */
public class StringRedisTemplateFactory implements FactoryBean<StringRedisTemplate> {
    private static final Logger logger = LoggerFactory.getLogger(StringRedisTemplateFactory.class);
    private JedisPoolConfig jedisPoolConfig;
    private String clusters;

    @Override
    public StringRedisTemplate getObject() throws Exception {
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
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        JedisUtils2.clusterNodes(this, connectionFactory);
        return redisTemplate;
    }

    @Override
    public Class getObjectType() {
        return StringRedisTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }
}
