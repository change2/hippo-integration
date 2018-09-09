package org.change.hippo.server.selector;

import com.alibaba.fastjson.JSON;
import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备类型验证
 */
public class PrivilegeSelector implements MessageSelector, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeSelector.class);

    @Resource
    private ApolloConfig apolloConfig;

    private Map<String, Boolean> appContexts = new ConcurrentHashMap<>(2);

    @Override
    public boolean accept(Message<?> message) {
        //检查开关,默认不检查(true)
        if (AcmsUtil.switchON(this)) {
            return true;
        }
        MessageHeaders headers = message.getHeaders();
        String v = headers.get(Constants.X_SERVICE_ID, String.class);
        String appContext = headers.get(Constants.X_APP_CONTEXT, String.class);
        if (v == null || appContext == null) {
            return true;
        }
        try {
            return !CollectionUtils.isEmpty(appContexts) && appContexts.containsKey(appContext);
        } catch (Exception e) {
            logger.error("Judge privilege error!appContext={},messageId={}", appContext, v, e);
            return true;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List apps = JSON.parseObject(apolloConfig.portalPrivilegesAppContext, List.class);
        if (null == apps || apps.size() == 0) {
            appContexts = new ConcurrentHashMap<String, Boolean>(2);
            return;
        }
        appContexts.clear();
        for (int i = 0, len = apps.size(); i < len; i++) {
            String app = (String) apps.get(i);
            appContexts.put(app, true);
        }
    }
}
