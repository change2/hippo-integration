package org.change.hippo.server.factory.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.change.hippo.server.pool.http.HttpServicePool;
import org.change.hippo.server.util.ColumnUtils;
import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.model.ServiceConfig;
import org.change.hippo.server.model.http.HttpConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: change.long
 * Date: 2017/11/20
 * Time: 下午10:56
 */
public class HttpServicePoolFactory implements ServicePoolFactory<HttpService> {

    private final Logger logger = LoggerFactory.getLogger(HttpServicePoolFactory.class);

    @Resource
    private ApolloConfig apolloConfig;
    private List<ServiceConfig> configs;
    @Override
    public ServicePool create(List<ServiceConfig> configs) throws Exception {
        if (configs == null || configs.isEmpty()) {
            return null;
        }

        HttpServicePool pool = new HttpServicePool();
        for (int i = 0; i < configs.size(); i++) {
            HttpConfig config = (HttpConfig) configs.get(i);
            HttpService httpService = new HttpService();
            httpService.postConstruct(config);
            httpService.init(config);
            pool.addService(config.getServiceId(), httpService);
        }
        getApolloConfig(pool);
        return pool;
    }

    public void getApolloConfig(HttpServicePool pool) {
        try {
            if (StringUtils.isBlank(apolloConfig.httpConfig) || "null".equals(apolloConfig.httpConfig.toLowerCase()))
                return;
            List<HttpConfig> list = JSON.parseArray(apolloConfig.httpConfig, HttpConfig.class);
            for (HttpConfig config : list) {
                logger.info("初始化Apollo配置接口   编号={},info={}",config.getServiceId(),JSON.toJSONString(config));
                for (String src : ColumnUtils.columns(config.getDomain())) {
                    config.setDomain(config.getDomain().replace(String.format("{%s}", src), System.getenv(src)));
                }
                HttpService httpService = new HttpService();
                httpService.postConstruct(config);
                httpService.init(config);
                pool.addService(config.getServiceId(), httpService);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    private void init() throws Exception {
        this.create(configs);
    }

    public void setConfigs(List<ServiceConfig> configs) {
        this.configs = configs;
    }
}
