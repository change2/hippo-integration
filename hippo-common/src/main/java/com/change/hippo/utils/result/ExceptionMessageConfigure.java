package com.change.hippo.utils.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: change.long
 * Date: 2017/8/7
 * Time: 下午3:05
 */
public class ExceptionMessageConfigure {

    /**
     * 编码
     */
    public static final String UTF8 = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMessageConfigure.class);
    private static final Map<String, String> EXCEPTION_MESSAGE = new ConcurrentHashMap<String, String>();

    public ExceptionMessageConfigure() {
    }

    public static boolean appendConfigure(String key, String message) {
        return appendConfigure(key, message, false);
    }

    public static boolean appendConfigure(String key, String message, boolean override) {
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(message)) {
            if (EXCEPTION_MESSAGE.containsKey(key)) {
                if (!override) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("已存在同名异常信息配置:{key=[{}] , message=[{}]},操作无效", key, EXCEPTION_MESSAGE.get(key));
                    }

                    return false;
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("覆盖已有异常信息配置:{key=[{}] , 原message=[{}], 新message=[{}]}", key, EXCEPTION_MESSAGE.get(key), message);
                }
            }

            EXCEPTION_MESSAGE.put(key, message);
            return true;
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("无效的异常信息配置:{key={} , message={}}", key, message);
            }

            return false;
        }
    }

    private static void addResource(Resource[] resources, List<Resource> resourceList) {
        if (resources != null && resources.length > 0) {
            resourceList.addAll(Arrays.asList(resources));
        }

    }

    public static String getMessage(String key) {
        return StringUtils.isEmpty(key) ? null : EXCEPTION_MESSAGE.get(key);
    }

    static {
        List<Resource> resourceList = new ArrayList<Resource>();

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            addResource(resolver.getResources("classpath:META-INF/**/exception/*.properties"), resourceList);
            addResource(resolver.getResources("classpath:META-INF/**/exception-*.properties"), resourceList);
            addResource(resolver.getResources("classpath:exception/**/*.properties"), resourceList);
            addResource(resolver.getResources("classpath:**/exception-*.properties"), resourceList);
        } catch (Exception var5) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.info("扫描异常消息配置文件失败", var5);
            }
        }

        if (resourceList.isEmpty()) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("未找到任何异常消息配置文件");
            }
        } else {
            Properties properties = new Properties();

            for (int i = resourceList.size() - 1; i >= 0; --i) {
                InputStreamReader reader = null;
                Resource resource = resourceList.get(i);
                try {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("读取异常消息配置文件[" + resource.getDescription() + "]");
                    }
                    reader = new InputStreamReader(resource.getInputStream(), UTF8);
                    properties.load(reader);
                } catch (Exception e) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("读取异常消息配置文件[" + resource.getDescription() + "]失败", e);
                    }
                } finally {
                    if (null != reader) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            if (LOGGER.isWarnEnabled()) {
                                LOGGER.warn("关闭异常消息配置文件[" + resource.getDescription() + "]失败", e);
                            }
                        }
                    }
                }
            }

            Enumeration enumeration = properties.propertyNames();

            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                appendConfigure(key, properties.getProperty(key));
            }
        }

    }
}
