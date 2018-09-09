package com.change.hippo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;

public class JedisClusterFactory implements FactoryBean<JedisCluster> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterFactory.class);
    private String clusters;
    private JedisPoolConfig jedisPoolConfig;
    private int timeout;
    private JedisCluster jedisCluster;
    private Integer maxAttempts = 5;

    public String getClusters() {
        return clusters;
    }

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    @Override
    public JedisCluster getObject() {
        HashSet<HostAndPort> nodes = new HashSet<HostAndPort>();
        String[] args = clusters.split(";");
        for (String arg : args) {
            String[] info = arg.split(":");
            nodes.add(new HostAndPort(info[0], Integer.valueOf(info[1])));
        }
        jedisCluster = new JedisCluster(nodes, timeout, timeout, maxAttempts, jedisPoolConfig);
        JedisUtils.detectJedisCluster(jedisCluster);
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return JedisCluster.class;
    }

    public int getTimeout() {
        return timeout;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
