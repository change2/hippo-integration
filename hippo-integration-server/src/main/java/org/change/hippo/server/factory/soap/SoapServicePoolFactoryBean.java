package org.change.hippo.server.factory.soap;

import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.factory.ServicePoolFactoryBean;
import org.change.hippo.server.parser.strategy.ServiceConverter;
import org.change.hippo.server.parser.strategy.SoapServiceConverter;


/**
 * 简化配置，创建soap pool对象
 */
public class SoapServicePoolFactoryBean extends ServicePoolFactoryBean {


    @Override
    protected ServicePoolFactory getSoapServicePoolFactory() {
        return new SoapServicePoolFactory();
    }

    @Override
    protected ServiceConverter getServiceConverter() {
        return new SoapServiceConverter();
    }
}
