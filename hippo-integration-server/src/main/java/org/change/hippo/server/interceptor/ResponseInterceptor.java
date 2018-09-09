package org.change.hippo.server.interceptor;

import com.change.hippo.utils.http.IPUtil;
import com.change.hippo.utils.ri.RequestUtils;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.change.hippo.server.util.Messages.TIMES;

/**
 * 响应拦截器
 */
public class ResponseInterceptor extends ChannelInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class);

    private final static String FAILED_LOG = "Service Processing failed!{}";
    private final static String SUCCESS_LOG = "Service Processing succeed!{}";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        recordLogs(message);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return message;
    }

    /**
     * @param message 记录响应日志信息
     */
    private void recordLogs(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        try {
            String code = headers.get(Constants.X_SERVICE_CODE, String.class);
            Map<String, Object> map = new HashMap<String, Object>();
            copyProperties(map, message);
            if (!"00".equals(code)) {
                logger.error(FAILED_LOG, map);
            } else {
                logger.info(SUCCESS_LOG, map);
            }
        } catch (Exception e) {
        }
    }

    private void copyProperties(Map<String, Object> map, Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Long responseTime = System.currentTimeMillis();
        String now = TIMES.get().format(new Date(responseTime));
        map.put("serviceId", headers.get(Constants.X_SERVICE_ID));
        map.put("messageId", headers.get(Constants.X_MESSAGE_ID));
        map.put("clientIp", RequestUtils.getRemoteIP(Messages.getRequest()));
        map.put("appContext", headers.get(Constants.X_APP_CONTEXT));

        Long receiveTime = headers.get(Constants.RECEIVE_TIME, Long.class);
        if (receiveTime != null) {
            map.put(Constants.RECEIVE_TIME, TIMES.get().format(new Date(receiveTime)));
            map.put("cost", responseTime - receiveTime);
        }
        map.put(Constants.RESPONSE_TIME, now);
        map.put("node", IPUtil.getServerIp());
        map.put("serviceMsg", headers.get(Constants.X_SERVICE_MESSAGE));
        map.put(Constants.USER_SESSION_KEY, headers.get(Constants.USER_SESSION_KEY));
        map.put(MessageHeaders.ID, headers.get(MessageHeaders.ID));
        map.put("response", message.getPayload());
        map.put(Constants.REMAINING_HEADER, Messages.getResponseHeader(Constants.REMAINING_HEADER));
        map.put(Constants.REPLENISH_RATE_HEADER, Messages.getResponseHeader(Constants.REPLENISH_RATE_HEADER));
        map.put(Constants.BURST_CAPACITY_HEADER, Messages.getResponseHeader(Constants.BURST_CAPACITY_HEADER));
    }

}
