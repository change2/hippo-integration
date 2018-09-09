package org.change.hippo.server.pool;

import org.change.hippo.server.service.Service;

import java.util.Collection;

public interface ServicePool {

    boolean addService(String id, Service service);

    Service getService(String id);

    boolean delService(String id);

    Collection<Service> getPool();

    boolean isEmpty();

}
