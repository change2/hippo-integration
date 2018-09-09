package org.change.hippo.server.activator;

import com.alibaba.fastjson.JSON;
import org.change.hippo.server.util.Constants;
import org.change.hippo.server.hystrix.PortalHystrixCommand;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;
import org.change.hippo.server.util.Messages;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class GatewayHandler {

    private ServicePool servicePool;

    public Message<String> handleMessage(Message<String> message) throws Exception {
        try {
            String serviceId = message.getHeaders().get(Constants.X_SERVICE_ID, String.class);
            Service<?> service = servicePool.getService(serviceId);
            String p = Messages.decrypt(message.getPayload());
            Object invoke;
            if (service.isCircuitBreakerEnabled()) {
                invoke = new PortalHystrixCommand(service, p).execute();
            } else {
                invoke = service.invoke(p);
            }
            String payload = invoke instanceof String ? (String) invoke : JSON.toJSONString(invoke);
            return MessageBuilder.withPayload(payload)
                    .setHeader(Constants.X_SERVICE_CODE, Constants.OK)
                    .setHeader(Constants.X_SERVICE_MESSAGE, Constants.OK_MSG)
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    public ServicePool getServicePool() {
        return servicePool;
    }

    public void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }

}