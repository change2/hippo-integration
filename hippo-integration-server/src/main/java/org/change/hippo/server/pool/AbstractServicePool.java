package org.change.hippo.server.pool;

import org.change.hippo.server.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractServicePool implements ServicePool {

    private final Logger logger = LoggerFactory.getLogger(AbstractServicePool.class);

    private Map<String, Service> services = new ConcurrentHashMap<String, Service>();

    @Override
    public boolean addService(String id, Service service) {
        services.put(id, service);
        logger.info("export Service id={} succeed,service={}", id, service);
        return services.get(id) != null;
    }

    @Override
    public Service getService(String id) {
        Service service = services.get(id);
        if (service == null) {
            throw new IllegalArgumentException(String.format("Service [%s] is not found.", id));
        }
        return service;
    }

    @Override
    public boolean delService(String id) {
        Service service = services.get(id);
        if (service != null) {
            logger.info("delete Service id={} succeed,service={}", id, service);
            services.remove(id);
            return true;
        }
        return false;
    }


    @Override
    public Collection<Service> getPool() {
        return Collections.unmodifiableCollection(services.values());
    }

    @Override
    public boolean isEmpty() {
        return services.isEmpty();
    }

}
