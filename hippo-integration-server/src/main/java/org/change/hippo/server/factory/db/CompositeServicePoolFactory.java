package org.change.hippo.server.factory.db;

import org.change.hippo.server.util.SpringContextUtils;
import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.model.ServiceConfig;
import org.change.hippo.server.parser.database.MySQLParser;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.pool.ServicePoolMerger;
import org.change.hippo.server.service.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

public class CompositeServicePoolFactory implements ServicePoolFactory {

    private ServicePoolMerger servicePool;

    private Map<String, String> serviceDefinedMap;

    public void init() throws Exception {
        MySQLParser parser = SpringContextUtils.getBeanByType(MySQLParser.class);
        this.create(parser.parse());
    }

    @Override
    public ServicePool create(List configs) throws Exception {
        for (Object config : configs) {
            if (serviceDefinedMap == null) {
                return null;
            }
            String clazz = serviceDefinedMap.get(config.getClass().getSimpleName());
            if (clazz == null) {
                continue;
            }
            Object instantiate = BeanUtils.instantiate(Class.forName(clazz));
            Service instantToUse = (Service) instantiate;
            ServiceConfig configToUse = (ServiceConfig) config;
            BeanUtils.copyProperties(config, instantToUse);
            instantToUse.init(configToUse);
            String serviceId = configToUse.getServiceId();
            this.servicePool.addService(serviceId, instantToUse);
        }
        return servicePool;
    }

    public void setServicePool(ServicePoolMerger servicePool) {
        this.servicePool = servicePool;
    }


    public void setServiceDefinedMap(Map<String, String> serviceDefinedMap) {
        this.serviceDefinedMap = serviceDefinedMap;
    }
}
