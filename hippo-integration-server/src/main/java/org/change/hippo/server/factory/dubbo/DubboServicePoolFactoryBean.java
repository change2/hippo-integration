package org.change.hippo.server.factory.dubbo;

import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.factory.ServicePoolFactoryBean;
import org.change.hippo.server.parser.strategy.DubboServiceConverter;
import org.change.hippo.server.parser.strategy.ServiceConverter;

public class DubboServicePoolFactoryBean extends ServicePoolFactoryBean {

    @Override
    protected ServicePoolFactory getSoapServicePoolFactory() {
        return new DubboServicePoolFactory();
    }

    @Override
    protected ServiceConverter getServiceConverter() {
        return new DubboServiceConverter();
    }


}
