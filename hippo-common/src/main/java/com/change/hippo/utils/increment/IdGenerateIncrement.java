package com.change.hippo.utils.increment;

/**
 * ID生成的worker
 */
public class IdGenerateIncrement {

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;

    private long twepoch = 1288834974657L;

    // 机器标示位数
    private long workerIdBits = 5L;
    // 数据中心标示位数
    private long datacenterIdBits = 5L;
    // 机器ID最大值
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 数据中心ID最大值
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 毫秒内自增ID
    private long sequenceBits = 12L;
    // 机器ID左移12位
    private long workerIdShift = sequenceBits;
    // 数据中心ID左移17位
    private long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间毫秒左移22位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    /**
     * 构造函数 定义 idworker的位数
     * @param workerId
     * @param datacenterId
     */
    public IdGenerateIncrement(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public IdGenerateIncrement() {
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            // 当前毫秒内则+1
            sequence = (sequence + 1) & sequenceMask;
            // 当毫秒内计数满了 等待下一秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;

        long nextId = ((timestamp - twepoch) << timestampLeftShift);
        // 生成最终的ID
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    long timeGen() {
        return System.currentTimeMillis();
    }

}