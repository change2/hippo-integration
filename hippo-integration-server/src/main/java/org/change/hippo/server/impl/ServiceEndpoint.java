package org.change.hippo.server.impl;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.utils.result.StatusCode;
import org.change.hippo.server.factory.ServicePoolFactory;
import com.change.hippo.maintain.model.RouteDefinition;
import com.change.hippo.maintain.service.IServiceEndpoint;
import org.change.hippo.server.parser.strategy.ServiceConverter;
import org.change.hippo.server.pool.ServicePoolMerger;
import org.change.hippo.server.service.Service;
import org.change.hippo.server.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;


public class ServiceEndpoint implements IServiceEndpoint, InitializingBean {

    private Map<String, ServiceConverter> serviceConverters;

    private ServicePoolFactory servicePoolFactory;

    private ServicePoolMerger servicePoolMerger;

    @Override
    public ActionResult<Boolean> addService(List<RouteDefinition> routeDefinitions) throws Exception {
        List routes = Lists.newArrayList();
        for (RouteDefinition routeVO : routeDefinitions) {
            ServiceConverter serviceConverter = serviceConverters.get(routeVO.getServiceModel());
            if (serviceConverter != null) {
                Map<String, Object> properties = PropertyUtils.describe(routeVO);
                Properties propertiesToUse = new Properties();
                for (Object key : properties.keySet()) {
                    if (key.equals("class")) continue;
                    Object value = properties.get(key);
                    if (value != null) {
                        propertiesToUse.put(routeVO.getServiceId() + "." + key, value);
                    }
                }
                routes.addAll(serviceConverter.convert(propertiesToUse.entrySet()));
            }
        }
        servicePoolFactory.create(routes);
        return new ActionResult<>(StatusCode.SUCCESS);
    }

    @Override
    public ActionResult<Boolean> delService(List<String> serviceIds) {
        for (String serviceId : serviceIds) {
            servicePoolMerger.delService(serviceId);
        }
        return new ActionResult<>(StatusCode.SUCCESS);
    }

    @Override
    public ActionResult<Collection<RouteDefinition>> find(List<String> serviceIds) {
        List<RouteDefinition> result = Lists.newArrayList();
        Collection<Service> pool = Lists.newArrayList();
        if (serviceIds == null || serviceIds.size() == 0) {
            pool = servicePoolMerger.getPool();
        }

        if (serviceIds != null && serviceIds.size() > 0) {
            for (String serviceId : serviceIds) {
                Service service = servicePoolMerger.getService(serviceId);
                pool.add(service);
            }
        }

        for (Service service : pool) {
            RouteDefinition target = new RouteDefinition();
            BeanUtils.copyProperties(service, target);
            BeanUtils.copyProperties(service.getServiceConfig(), target);
            target.setParam(service.getParam() != null ? Arrays.toString(service.getParam()) : "");
            result.add(target);
        }
        return new ActionResult<>(StatusCode.SUCCESS, result);
    }

    @Override
    public void afterPropertiesSet() {
        this.serviceConverters = SpringContextUtils.getBeans(ServiceConverter.class);
    }

    public void setServicePoolFactory(ServicePoolFactory servicePoolFactory) {
        this.servicePoolFactory = servicePoolFactory;
    }

    public void setServicePoolMerger(ServicePoolMerger servicePoolMerger) {
        this.servicePoolMerger = servicePoolMerger;
    }
}
