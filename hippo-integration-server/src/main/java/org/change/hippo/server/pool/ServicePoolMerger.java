package org.change.hippo.server.pool;

import org.change.hippo.server.service.Service;

import java.util.Collection;
import java.util.Iterator;

public class ServicePoolMerger extends AbstractServicePool {

    private final Collection<ServicePool> servicePools;

    public ServicePoolMerger(Collection<ServicePool> servicePools) {
	    this.servicePools = servicePools;
		for (ServicePool servicePool : servicePools) {
			merge(servicePool);
		}
	}

	public boolean merge(ServicePool servicePool) {
		if (servicePool == null || servicePool.isEmpty()) {
			return Boolean.TRUE;
		}

		Iterator iterator = servicePool.getPool().iterator();
		while (iterator.hasNext()) {
			Service service = (Service) iterator.next();
			super.addService(service.getServiceId(), service);
		}

		return Boolean.TRUE;
	}

    public Collection<ServicePool> getServicePools() {
        return servicePools;
    }
}
