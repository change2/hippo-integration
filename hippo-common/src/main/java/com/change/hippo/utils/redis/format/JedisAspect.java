package com.change.hippo.utils.redis.format;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import com.change.hippo.utils.redis.format.JedisApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Author: Tao.Wang
 * Date: 2017/11/24
 * Time: 上午12:55
 * Org : 思笛恩
 * To change this template use File | Settings | File Templates.
 * Description:
 *
 * @author: Tao.Wang
 */
@Aspect
@Component
public class JedisAspect {

    Logger logger = LoggerFactory.getLogger(JedisAspect.class);

    private static final String APP_ID_KEY = "app.id";

    /**
     * 修改redis的key为规范的key
     *
     * @param joinPoint 该参数可以获取目标对象的信息,如类名称,方法参数,方法名称等
     */

    @Around("within(redis.clients.jedis.JedisCommands+)")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            String keyHeader = JedisApplicationConfig.getStringProperty(APP_ID_KEY) + ":";

            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                if (args[0] instanceof String) {
                    args[0] = keyHeader + args[0];
                } else if (args[0] instanceof Integer && args.length > 1) {
                    args[1] = keyHeader + args[1];
                } else {
                    logger.warn("参数不正确：{}", JSON.toJSONString(args, true));
                }
            }
            Object result = joinPoint.proceed(args);
            logger.debug(String.format("redis format 参数%s", JSON.toJSONString(args, true)));
            return result;
        } catch (Throwable e) {
            logger.error("格式化key异常", e);
            return null;
        }
    }
}
