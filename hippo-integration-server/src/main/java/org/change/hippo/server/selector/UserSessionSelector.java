package org.change.hippo.server.selector;

import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;
import org.change.hippo.server.sso.SSOManager;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * User: change.long
 * Date: 2017/11/21
 * Time: 下午8:24
 * 用户是否登录拦截
 */
public class UserSessionSelector implements MessageSelector {


    @Resource
    private ApolloConfig apolloConfig;

    private ServicePool servicePool;

    @Override
    public boolean accept(Message<?> message) {
        //检查开关,默认不检查(true)
        if (AcmsUtil.switchON(this)) {
            return true;
        }
        MessageHeaders headers = message.getHeaders();
        String serviceId = headers.get(Constants.X_SERVICE_ID, String.class);
        Service<?> service = servicePool.getService(serviceId);
        if (service.isPermission()) {
            String userSessionKey = headers.get(Constants.USER_SESSION_KEY, String.class);
            return SSOManager.isLogin(userSessionKey);
        }
        return true;
    }

    public void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }
}
