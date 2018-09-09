package org.change.hippo.server.selector;

import org.change.hippo.server.util.Constants;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;


public class ServiceIdSelector implements MessageSelector {

	@Override
	public boolean accept(Message<?> message) {
        String serviceId = message.getHeaders().get(Constants.X_SERVICE_ID, String.class);
        return StringUtils.hasText(serviceId);
	}

}
