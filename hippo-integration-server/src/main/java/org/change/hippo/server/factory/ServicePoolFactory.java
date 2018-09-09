package org.change.hippo.server.factory;

import org.change.hippo.server.model.ServiceConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;

import java.util.List;

public interface ServicePoolFactory<T extends Service<?>> {

	ServicePool create(List<ServiceConfig> configs) throws Exception;

}
