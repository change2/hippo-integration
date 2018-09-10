package org.change.hippo.server.factory.http;

import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.factory.ServicePoolFactoryBean;
import org.change.hippo.server.parser.strategy.HttpServiceConverter;
import org.change.hippo.server.parser.strategy.ServiceConverter;

public class HttpServicePoolFactoryBean extends ServicePoolFactoryBean {

    @Override
    protected ServicePoolFactory getSoapServicePoolFactory() {
        return new HttpServicePoolFactory();
    }

    @Override
    protected ServiceConverter getServiceConverter() {
        return new HttpServiceConverter();
    }

}
