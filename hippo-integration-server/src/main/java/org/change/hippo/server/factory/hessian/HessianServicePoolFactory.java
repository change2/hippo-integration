package org.change.hippo.server.factory.hessian;

import org.change.hippo.server.pool.hessian.HessianServicePool;
import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.model.ServiceConfig;
import org.change.hippo.server.model.hessian.HessianServiceConfig;
import org.change.hippo.server.service.hessian.HessianService;

import java.util.List;

public class HessianServicePoolFactory implements ServicePoolFactory<HessianService> {

	@Override
    public HessianServicePool create(List<ServiceConfig> configs) throws Exception {
		if (configs == null || configs.isEmpty()) {
			return null;
		}

		HessianServicePool pool = new HessianServicePool();
		for (int i = 0; i < configs.size(); i++) {
			HessianServiceConfig config = (HessianServiceConfig) configs.get(i);
			HessianService hessianService = new HessianService();
            hessianService.postConstruct(config);
			hessianService.init(config);
			pool.addService(config.getServiceId(), hessianService);
		}
		return pool;
	}
}
