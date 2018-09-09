package org.change.hippo.server.selector;

import org.apache.commons.lang3.StringUtils;
import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.util.ParamCheckUtils;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

public class CheckParamSelector implements MessageSelector {

    private ServicePool servicePool;

    @Resource
    private ApolloConfig apolloConfig;

    @Override
    public boolean accept(Message<?> message) {
        try {
            //检查开关,默认不检查(true)
            if (AcmsUtil.switchON(this)) {
                return true;
            }
            MessageHeaders headers = message.getHeaders();
            String p = (String) message.getPayload();
            String serviceId = headers.get(Constants.X_SERVICE_ID, String.class);
            Service<?> service = servicePool.getService(serviceId);
            String[] param = service.getParam();
            if (StringUtils.isBlank(p) || param == null || ParamCheckUtils.checkParam(p, param)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }
}
