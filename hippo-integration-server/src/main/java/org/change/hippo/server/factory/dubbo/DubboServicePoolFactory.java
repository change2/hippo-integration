package org.change.hippo.server.factory.dubbo;

import org.change.hippo.server.pool.dubbo.DubboServicePool;
import org.change.hippo.server.factory.ServicePoolFactory;
import org.change.hippo.server.model.dubbo.DubboServiceConfig;
import org.change.hippo.server.pool.ServicePool;
import org.change.hippo.server.service.dubbo.DubboService;

import java.util.List;

/**
 * User: change.long
 * Date: 2017/11/23
 * Time: 下午2:23
 */
public class DubboServicePoolFactory implements ServicePoolFactory<DubboService> {

    @Override
    public ServicePool create(List configs) throws Exception {
        if (configs == null || configs.isEmpty()) {
            return null;
        }

        DubboServicePool pool = new DubboServicePool();
        for (int i = 0; i < configs.size(); i++) {
            DubboServiceConfig config = (DubboServiceConfig) configs.get(i);
            DubboService dubboService = new DubboService();
            dubboService.postConstruct(config);
            dubboService.init(config);
            pool.addService(config.getServiceId(), dubboService);
        }

        return pool;
    }

}
