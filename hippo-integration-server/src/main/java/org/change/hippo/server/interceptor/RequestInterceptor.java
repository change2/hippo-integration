package org.change.hippo.server.interceptor;

import org.change.hippo.server.util.Constants;
import org.change.hippo.server.util.Messages;
import org.change.hippo.server.sso.SSOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.util.HashMap;
import java.util.Map;

public class RequestInterceptor extends ChannelInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
    private final static String REQUEST_LOG = "Invoking Request with={}";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //添加请求时间，方便打印日志
        long requestTime = System.currentTimeMillis();
        Message<?> build = MessageBuilder.fromMessage(message)
                .setHeaderIfAbsent(Constants.RECEIVE_TIME, requestTime)
                .build();
        recordLogs(message);
        return build;

    }

    /**
     * 记录请求日志信息
     */
    private void recordLogs(Message<?> message) {
        String serviceId = (String) message.getHeaders().get(Constants.X_SERVICE_ID);
        String mobile = SSOManager.getMobile((String) message.getHeaders().get(Constants.USER_SESSION_KEY));
        String appContext = (String) message.getHeaders().get(Constants.X_APP_CONTEXT);
        long requestTime = System.currentTimeMillis();
        String payload = Messages.decrypt((String) message.getPayload());
        //解密调用后台接口
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("serviceId", serviceId);
        map.put("requestTime", Messages.TIMES.get().format(requestTime));
        map.put("mobile", mobile);
        map.put("appContext", appContext);
        map.put("request", payload);
        logger.info(REQUEST_LOG, map);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return message;
    }

}
