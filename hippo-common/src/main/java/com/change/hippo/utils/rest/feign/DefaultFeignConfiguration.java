package com.change.hippo.utils.rest.feign;

import feign.Logger;
import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 默认feign client配置
 */
@Configuration
public class DefaultFeignConfiguration {

    /**
     * 连接建立超时时间 10秒
     */
    public static final int CONNECT_TIMEOUT_MILLIS = 10 * 1000;

    /**
     * 读取数据超时时间 30秒
     */
    public static final int READ_TIMEOUT_MILLIS = 30 * 1000;

    /**
     * 默认的日志等级。只打印URL还有花费时间
     */
    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.BASIC;
    }

    /**
     * 设置请求参数
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(CONNECT_TIMEOUT_MILLIS, READ_TIMEOUT_MILLIS);
    }

    /**
     * base64 认证拦截器
     */
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "admin");
    }

    /**
     * rid链路跟踪 认证拦截器
     */
    @Bean
    public RidFeignRequestInterceptor ridFeignRequestInterceptor() {
        return new RidFeignRequestInterceptor();
    }

}
