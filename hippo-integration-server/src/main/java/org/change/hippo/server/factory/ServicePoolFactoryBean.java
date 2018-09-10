package org.change.hippo.server.factory;

import org.change.hippo.server.parser.file.PropertiesParser;
import org.change.hippo.server.parser.strategy.ServiceConverter;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.Service;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

public abstract class ServicePoolFactoryBean implements FactoryBean<ServicePool> {

    private String path;

    @Override
    public ServicePool getObject() throws Exception {
        PropertiesParser propertiesParser = new PropertiesParser<>();
        propertiesParser.setPath(path);
        propertiesParser.setServiceConverter(getServiceConverter());
        List parseLists = propertiesParser.parse();
        return getSoapServicePoolFactory().create(parseLists);
    }

    protected abstract ServicePoolFactory getSoapServicePoolFactory();

    protected abstract ServiceConverter getServiceConverter();

    @Override
    public Class<?> getObjectType() {
        return ServicePool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
