package com.change.hippo.utils.redis;

/**
 * User: change.long
 * Date: 2017/9/23
 * Time: 上午10:07
 */
public enum  CacheTimes {
    /**
     * 一天
     */
    ONEDAY(86400,"一天"),

    /**
     * 不过期
     */
    FOREVER(-2,"不过期");

    private final String desc;
    private int time;

    private CacheTimes(int time, String desc) {
        this.time = time;
        this.desc =  desc;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
