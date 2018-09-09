package org.change.hippo.server.service.dubbo;

import org.apache.commons.lang.StringUtils;
import org.change.hippo.server.util.ClassUtils;
import org.change.hippo.server.util.JsonUtils;
import org.change.hippo.server.util.SpringContextUtils;
import org.change.hippo.server.model.dubbo.DubboServiceConfig;
import org.change.hippo.server.service.AbstractService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DubboService extends AbstractService<DubboServiceConfig> {
    private DubboServiceConfig dubboServiceConfig;
    private Method method;
    private Object bean;

    @Override
    public boolean init(DubboServiceConfig dubboServiceConfig) throws Exception {

        this.dubboServiceConfig = dubboServiceConfig;
        String methodName = dubboServiceConfig.getMethod();
        String argumentType[] = dubboServiceConfig.getArguments();
        bean = SpringContextUtils.getDirectImplBean(dubboServiceConfig.getInterfaze());
        method = bean.getClass().getMethod(methodName, ClassUtils.forNames(argumentType));
        return Boolean.TRUE;
    }

    @Override
    public Object invoke(String args) throws Exception {
        Class[] classes = ClassUtils.forNames(dubboServiceConfig.getArguments());

        List list;
        if (StringUtils.isBlank(args)) {
            list = new ArrayList();
        } else if (JsonUtils.isObject(args)) {
            list = new ArrayList();
            list.add(JsonUtils.readValue(args, classes[0]));
        } else {
            list = JsonUtils.readValue(args, List.class);
        }

        return method.invoke(bean, list.toArray());
    }

    public DubboServiceConfig getDubboServiceConfig() {
        return dubboServiceConfig;
    }

    public void setDubboServiceConfig(DubboServiceConfig dubboServiceConfig) {
        this.dubboServiceConfig = dubboServiceConfig;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
