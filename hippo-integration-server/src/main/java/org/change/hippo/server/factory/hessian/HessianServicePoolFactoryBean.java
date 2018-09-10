package org.change.hippo.server.factory.hessian;

import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.factory.ServicePoolFactoryBean;
import org.change.hippo.server.parser.strategy.HessianServiceConverter;
import org.change.hippo.server.parser.strategy.ServiceConverter;

public class HessianServicePoolFactoryBean extends ServicePoolFactoryBean {


    @Override
    protected ServicePoolFactory getSoapServicePoolFactory() {
        return new HessianServicePoolFactory();
    }

    @Override
    protected ServiceConverter getServiceConverter() {
        return new HessianServiceConverter();
    }
}
