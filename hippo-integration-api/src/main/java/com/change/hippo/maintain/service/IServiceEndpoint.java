package com.change.hippo.maintain.service;

import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.maintain.model.RouteDefinition;

import java.util.Collection;
import java.util.List;


public interface IServiceEndpoint {
    /**
     * 批量添加
     */

    ActionResult<Boolean> addService(List<RouteDefinition> routeDefinitions) throws Exception;

    /**
     * 批量按照serviceId删除
     */
    ActionResult<Boolean> delService(List<String> serviceIds);

    /**
     * 按照serviceId接口列表
     */
    ActionResult<Collection<RouteDefinition>> find(List<String> serviceIds);
}
