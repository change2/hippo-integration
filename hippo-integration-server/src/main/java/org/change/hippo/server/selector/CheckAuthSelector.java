package org.change.hippo.server.selector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.change.hippo.server.apollo.ApolloConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;
import org.change.hippo.server.sso.SSOManager;
import org.change.hippo.server.util.AcmsUtil;
import org.change.hippo.server.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * User: change.long
 * Date: 2017/11/24
 * Time: 下午4:54
 * 用户权限校验
 */
public class CheckAuthSelector implements MessageSelector {

    private static final Logger logger = LoggerFactory.getLogger(CheckAuthSelector.class);
    // 权限检验接口
    private static final String CHECK_AUTH_SERVICE_ID = "CA101001";
    private static final String OK = "ok";
    private static final String DATA = "data";

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
        String appContext = headers.get(Constants.X_APP_CONTEXT, String.class);
        String sessionId = headers.get(Constants.USER_SESSION_KEY, String.class);
        if (serviceId == null || appContext == null) {
            return true;
        }
        try {
            Service<?> service = servicePool.getService(serviceId);
            String authCode = service.getActionType();
            if (authCode == null || "".equals(authCode)) {
                return true;
            }
            // 管理员权限验证
            if (authCode.equals("0")) {
                return SSOManager.getUserIsSuper(sessionId);
            }
            Map<String, String> params = new HashMap<String, String>(2);
            params.put(Constants.SYS_USER_SESSION_KEY, sessionId);
            params.put(Constants.ACTION_TYPE_NAME, authCode);
            Service<?> authHttpService = servicePool.getService(CHECK_AUTH_SERVICE_ID);

            Object invoke = authHttpService.invoke(JSON.toJSONString(params));
            String payload = invoke instanceof String ? (String) invoke : JSON.toJSONString(invoke);
            JSONObject object = JSON.parseObject(payload);
            return object != null && object.getBoolean(OK) && object.getBoolean(DATA);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return true;
        }
    }

    public void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }
}
