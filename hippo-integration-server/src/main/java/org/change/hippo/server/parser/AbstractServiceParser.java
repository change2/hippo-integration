package org.change.hippo.server.parser;

import org.change.hippo.server.parser.strategy.ServiceConverter;
import org.change.hippo.server.model.ServiceConfig;

public abstract class AbstractServiceParser<T extends ServiceConfig> implements ServiceParser<T> {

	private ServiceConverter<T> serviceConverter;

	public ServiceConverter<T> getServiceConverter() {
		return serviceConverter;
	}

	public void setServiceConverter(ServiceConverter<T> serviceConverter) {
		this.serviceConverter = serviceConverter;
	}

}
