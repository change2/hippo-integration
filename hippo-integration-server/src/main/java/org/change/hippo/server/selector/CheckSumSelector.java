package org.change.hippo.server.selector;

import org.apache.commons.lang.StringUtils;
import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.model.SecuritySettings;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.util.JsonUtils;
import org.change.hippo.server.util.SignatureUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: change.long
 * Date: 2017/11/21
 * Time: 下午3:51
 * 签名验证
 */
public class CheckSumSelector implements MessageSelector, InitializingBean {

    private Environment environment;

    @Resource
    private ApolloConfig apolloConfig;

    private Map<String, SecuritySettings> securitySettings = new ConcurrentHashMap<String, SecuritySettings>();

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
        String contentSignature = headers.get(Constants.X_CONTENT_SIGNATURE, String.class);
        return checkSignature(appContext, contentSignature, (String) message.getPayload());
    }

    private boolean checkSignature(String appId, String contentSignature, String json) {
        if (StringUtils.isBlank(contentSignature)) {
            return false;
        }
        SecuritySettings securitySetting = securitySettings.get(appId);
        if (securitySetting == null || StringUtils.isBlank(securitySetting.getApiSecret())) {
            return false;
        }
        String signature = SignatureUtils.signature(securitySetting.getApiSecret(), json);
        return StringUtils.equals(signature, contentSignature);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SecuritySettings> securityArray = JsonUtils.readList(apolloConfig.checkSum, SecuritySettings.class);
        if (securityArray == null || securityArray.size() == 0) {
            securitySettings = new ConcurrentHashMap<String, SecuritySettings>();
            return;
        }
        securitySettings.clear();
        for (SecuritySettings settings : securityArray) {
            securitySettings.put(settings.getAppId(), settings);
        }
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
