package org.change.hippo.server.service.http;

import org.change.hippo.server.http.IntegrationClient;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.model.http.HttpConfig;
import org.change.hippo.server.service.AbstractService;

/**
 * User: change.long
 * Date: 2017/11/20
 * Time: 下午10:24
 */
public class HttpService extends AbstractService<HttpConfig> {


    private HttpConfig httpConfig;

    @Override
    public boolean init(HttpConfig httpConfig) throws Exception {
        this.httpConfig = httpConfig;
        setServiceId(httpConfig.getServiceId());
        setServiceConfig(httpConfig);
        String url = httpConfig.getDomain();
        IntegrationClient mbpIntegrationClient = ClientSubRegistry.get(url);
        if (mbpIntegrationClient == null) {
            mbpIntegrationClient = new IntegrationClient();
            mbpIntegrationClient.setAppName(Constants.FROM);
            mbpIntegrationClient.setURL(httpConfig.getDomain());
            mbpIntegrationClient.setGroup(Constants.DEFAULT_GROUP);
            mbpIntegrationClient.setMax(Constants.DEF_MAX);
            mbpIntegrationClient.setMethod(httpConfig.getMethod());
            mbpIntegrationClient.afterPropertiesSet();
            ClientSubRegistry.put(url, mbpIntegrationClient);
        }
        return ClientSubRegistry.get(url) != null;
    }

    @Override
    public Object invoke(String args) throws Exception {
        IntegrationClient mbpIntegrationClient = ClientSubRegistry.get(httpConfig.getDomain());
        return mbpIntegrationClient.httpRequest(args, httpConfig.getPath(), httpConfig.getMethod());
    }
}
