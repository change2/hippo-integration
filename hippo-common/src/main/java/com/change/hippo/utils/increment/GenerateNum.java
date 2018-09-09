package com.change.hippo.utils.increment;

import java.util.Random;

/**
 * Created by 种类 on 2016/5/26.
 * 此类针对简单的生成器 使用  如 并发很小的 等 如果使用最好 50毫秒slepp进行使用 否则会有重复的数据
 *
 */
public class GenerateNum {

    /**
     * 生成一个免密的字符串
     * @param prefix
     * @return
     */
    public static String generateOrderNm(String prefix){
        Random random = new Random();
        int m = random.nextInt(10);
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(1, s.length() - 2);
        StringBuilder append = new StringBuilder(prefix).append(substring).append(m);

        return append.toString();
    }
}
