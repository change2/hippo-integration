package org.change.hippo.server.factory.soap;

import org.change.hippo.server.pool.soap.SoapServicePool;
import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.model.ServiceConfig;
import org.change.hippo.server.model.soap.SoapServiceConfig;
import org.change.hippo.server.service.soap.SoapService;

import java.util.List;

public class SoapServicePoolFactory implements ServicePoolFactory<SoapService> {

	@Override
    public SoapServicePool create(List<ServiceConfig> configs) throws Exception {
		if (configs == null || configs.isEmpty()) {
			return null;
		}

		SoapServicePool pool = new SoapServicePool();
		for (int i = 0; i < configs.size(); i++) {
			SoapServiceConfig config = (SoapServiceConfig) configs.get(i);
			SoapService service = new SoapService();
            service.postConstruct(config);
			service.init(config);
			pool.addService(config.getServiceId(), service);
		}
		return pool;
	}
}
