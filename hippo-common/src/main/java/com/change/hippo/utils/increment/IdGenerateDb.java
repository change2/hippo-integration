package com.change.hippo.utils.increment;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 生成算法:来源(两位)+从2011-11-05开始的时间戳(12位)+毫秒增量(3位)+服务器标识(2位)
 */
@Deprecated
class IdGenerateDb {


    //缓存刷新key值，不同机器标识不同
    public static final String REFRESH_KEY = UUID.randomUUID().toString().replaceAll("-", "");

    private static final Queue<String> cacheOrderIdQueue = new ConcurrentLinkedQueue<String>();

    private static Random random = new Random();

    /**
     * 默认一次入队号数量
     */
    private static int DEFAULT_ENQUEUE_SIZE = 1000;

    /**
     * 队列订单号小于指定值，定时任务会进行一次号入队操作
     */
    private static int DEFAULT_ALARM_QUEUE_SIZE = 200;

    private static final String[] RANDOM_TABLE = {
            "9360754812", "5483921670", "7148503926",
            "0298436517", "8201465793", "1508293746",
            "3421750986", "3076452819", "8627953014",
            "0258491763"
    };

//    @PostConstruct
    public void init(){
        refreshSource();
        enqueueAndFetchOne(DEFAULT_ENQUEUE_SIZE, false);
    }

    /**
     * 根据码表及随机数，对字符串进行加密
     * @param text 必须由数字组成的字串
     * @param table 字符串必须由数字组成
     * @param key 0~9随机数
     * @param seed 0~9随机数
     * @return
     */
    private static String encode(String text, String table, int key, int seed) {
        StringBuilder buf = new StringBuilder();
        int m, tbLen = table.length(), len = text.length();
        for (int i = 0; i < len; i += 1) {
            m = table.indexOf(text.charAt(i));
            m = m + key + i;
            buf.append(table.charAt(m % tbLen));
        }
        buf.append(key).append(seed);
        return buf.toString();
    }

    private static String encode(long id){
        int key = random.nextInt(10);
        int seed = random.nextInt(10);
        //使用随机数生成随机码表
        String randomTable = RANDOM_TABLE[seed];
        String fid = id > 9999999L ? Long.toString(id) : String.format("%08d", id);
        return encode(fid, randomTable, key, seed);
    }

    /**
     * 生成count个订单号，并随机打乱顺序加入缓存队列中
     * @param count 入队订单数量
     * @param fetchOne 是否要返回一个订单号，保证高并发的情况下，当前线程始终能拿到一个订单号
     */
    private String enqueueAndFetchOne(int count, boolean fetchOne){
        long cur = System.currentTimeMillis();
//        try {
            long sequence = 0l ;// orderSequenceDao.getAndIncre(count);
            int c = count;
            List<String> sequenceList = new LinkedList<String>();
            while(--c > -1){
                sequenceList.add(encode(sequence + c));
            }
            for(c = count; c > 1; --c){
                int i = (int) (c * Math.random());
                String seq = sequenceList.remove(i);
                cacheOrderIdQueue.add(seq);
            }
            String seq = sequenceList.get(0);
            if(fetchOne){
                return seq;
            }else{
                cacheOrderIdQueue.add(seq);
                return null;
            }
//        } catch (SQLException e) {
//            log.error(String.format("Couldn't get sequence from database by count %s --> %s!", count, e.getMessage()), e);
//        }finally{
//            log.info("generate {} order id spent {}ms", count, System.currentTimeMillis() - cur);
//        }
    }

    private String generateId(String sourceId){
        String sequence = cacheOrderIdQueue.poll();
        if(sequence == null){
            sequence = enqueueAndFetchOne(DEFAULT_ENQUEUE_SIZE, true);
        }
        String id = sourceId + sequence;
        return id;
    }

    public String generate(String source){
        String sourceId = "";
        //orderSourceDao.getId(source);
        if (StringUtils.isBlank(sourceId)) {
//            log.warn("未定义的source({})!", source);
            return null;
        }
        return generateId(sourceId);
    }

    public String[] generate(String source, byte count) {
        String sourceId = "";
        //orderSourceDao.getId(source);
        if (StringUtils.isBlank(sourceId)) {
//            log.warn("未定义的source({})!", source);
            return null;
        }

        String[] ids = new String[count];
        while(--count > -1){
            ids[count] = generateId(sourceId);
        }

        return ids;
    }

    public String refreshSource() {
        String value = "";
        //orderPropertyService.getValue("orderIdEnqueueSize");
        if(StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value)){
            int enqueueSize = Integer.parseInt(value);
            if(enqueueSize > 1000){
                DEFAULT_ENQUEUE_SIZE = enqueueSize;
            }
        }
//        value = orderPropertyService.getValue("orderIdAlarmQueueSize");
        if(StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value)){
            int alarmSize = Integer.parseInt(value);
            if(alarmSize > 200){
                DEFAULT_ALARM_QUEUE_SIZE = alarmSize;
            }
        }
//        orderSourceDao.refreshSource();
        return REFRESH_KEY;
    }

    public void ensureQueueSize() {
        if(cacheOrderIdQueue.size() < DEFAULT_ALARM_QUEUE_SIZE){
            enqueueAndFetchOne(DEFAULT_ENQUEUE_SIZE, false);
        }
    }

}