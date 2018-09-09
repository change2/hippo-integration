package org.change.hippo.server.interceptor;

import com.change.hippo.utils.ri.RequestIdentityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * 请求链路跟踪过滤器配置
     */
    @Bean
    public FilterRegistrationBean ridFilter() {
        FilterRegistrationBean ridFilter = new FilterRegistrationBean();
        ridFilter.setFilter(new RequestIdentityFilter());
        return ridFilter;
    }
}