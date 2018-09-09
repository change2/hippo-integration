package com.change.hippo.admin.shiro;

/**
 * @author bootdo 1992lcg@163.com
 * @version V1.0
 */

import java.util.Set;

/**
 *
 */
public class RedisManager {

    private int expire = 0;

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] value = null;
        try {
            value = RedisUtil.jedisCluster.get(key);
        } catch (Exception e){

        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        try {
            RedisUtil.jedisCluster.set(key, value);
            if (this.expire != 0) {
                RedisUtil.jedisCluster.expire(key, this.expire);
            }
        } catch (Exception e){
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        try {
            RedisUtil.jedisCluster.set(key, value);
            if (expire != 0) {
                RedisUtil.jedisCluster.expire(key, expire);
            }
        } catch (Exception e){

        }
        return value;
    }

    /**
     * del
     *
     * @param key
     */
    public void del(byte[] key) {
        try {
            RedisUtil.jedisCluster.del(key);
        } catch (Exception e){

        }
    }

    /**
     * flush
     */
    public void flushDB() {
        try {
            RedisUtil.jedisCluster.flushDB();
        } catch (Exception e){

        }
    }

    /**
     * size
     */
    public Long dbSize() {
        Long dbSize = 0L;
        try {
            dbSize = RedisUtil.jedisCluster.dbSize();
        } catch (Exception e){

        }
        return dbSize;
    }

    /**
     * keys
     *
     * @param pattern
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        //Jedis jedis = jedisPool.getResource();
        try {
           // keys = jedis.keys(pattern.getBytes());
        } catch (Exception e){
            //if (jedis != null) {
           //     jedis.close();
          //  }
        }
        return keys;
    }


    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}
