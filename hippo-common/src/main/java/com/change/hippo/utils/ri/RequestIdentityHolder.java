package com.change.hippo.utils.ri;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 请求信息持有者
 *
 */
public final class RequestIdentityHolder {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestIdentityHolder.class);
    private final static ThreadLocalMap HOLDER = new ThreadLocalMap();
    private static String NAME = "";
    private static String VERSION = "";
    private final static String CONFIG = "META-INF/requestinfo.properties";

    static {
        try {
            ClassLoader classLoader = RequestIdentityHolder.class.getClassLoader();
            Enumeration<URL> urls;
            if (classLoader != null) {
                urls = classLoader.getResources(CONFIG);
            } else {
                urls = ClassLoader.getSystemResources(CONFIG);
            }
            if (urls != null) {
                Properties properties = new Properties();
                if (urls.hasMoreElements()) {
                    properties.load(urls.nextElement().openStream());
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("classpath:requestinfo.properties配置文件内容:{}", properties);
                }
                NAME = (String) properties.get("name");
                VERSION = (String) properties.get("version");
            }
        } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("加载classpath:requestinfo.properties文件失败", e);
            }
        }
    }

    /**
     * 私有构造方法,不允许外部构造
     */
    private RequestIdentityHolder() {
    }

    /**
     * 设置当前请求信息
     * @param requestInfo
     */
    public static void set(RequestInfo requestInfo) {
        if (requestInfo != null) {
            MDC.put("rid", requestInfo.getId());
            MDC.put("rstep", requestInfo.getStep() + "");
            MDC.put("rip", StringUtils.trimToEmpty(requestInfo.getIp()));
            MDC.put("rname", StringUtils.trimToEmpty(requestInfo.getName()));
            MDC.put("rversion", StringUtils.trimToEmpty(requestInfo.getVersion()));
            HOLDER.set(requestInfo);
        } else {
            clear();
        }
    }

    /**
     * 获取当前请求信息
     * @return
     */
    public static RequestInfo get() {
        return HOLDER.get();
    }

    /**
     * 获取当前请求信息
     * @param autoCreate 如果为null,是否自动创建并自动与当前线程绑定
     * @return
     */
    public static RequestInfo get(boolean autoCreate) {
        RequestInfo ri = get();
        if (ri == null && autoCreate) {
            ri = generateNew();
            set(ri);
        }
        return ri;
    }

    /**
     * 生成一个新的RequestInfo,但不自动与当前线程绑定
     * @return
     */
    public static RequestInfo generateNew() {
        RequestInfo requestInfo = new RequestInfo(generateRid());
        requestInfo.setName(NAME);
        requestInfo.setVersion(VERSION);
        return requestInfo;
    }

    public static String generateRid() {
        return RandomStringUtils.randomNumeric(10);
    }

    /**
     * 加入一个已存在请求调用序列
     * @param requestInfo 已存在请求调用序列的请求信息
     * @return 当前线程私有的请求信息, 不同于参数中指定的RequestInfo
     */
    public static RequestInfo join(RequestInfo requestInfo) {
        if (requestInfo != null) {
            requestInfo = requestInfo.clone();
            set(requestInfo);
        }
        return requestInfo;
    }

    public static void clear() {
        HOLDER.set(null);
        MDC.remove("rid");
        MDC.remove("rip");
        MDC.remove("rstep");
    }

    public static String getName() {
        return NAME;
    }

    public static String getVersion() {
        return VERSION;
    }

}