package org.change.hippo.server.service.hessian;

import org.change.hippo.server.util.ClassUtils;
import org.change.hippo.server.util.JsonUtils;
import org.change.hippo.server.model.hessian.HessianServiceConfig;
import org.change.hippo.server.service.AbstractService;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HessianService extends AbstractService<HessianServiceConfig> {

	private HessianServiceConfig hessianServiceConfig;
	private Method method;
	private Object bean;

	@Override
    public boolean init(HessianServiceConfig hessianServiceConfig) throws Exception {
		this.hessianServiceConfig = hessianServiceConfig;
        setServiceId(hessianServiceConfig.getServiceId());
        setServiceConfig(hessianServiceConfig);
		String methodName = hessianServiceConfig.getMethod();
		Class interfaze = Class.forName(hessianServiceConfig.getInterfaze());
		Class[] classes = ClassUtils.forNames(hessianServiceConfig.getArguments());

		HessianProxyFactoryBean hessianProxyFactoryBean = new HessianProxyFactoryBean();
		hessianProxyFactoryBean.setServiceUrl(hessianServiceConfig.getUrl());
		hessianProxyFactoryBean.setServiceInterface(interfaze);
		hessianProxyFactoryBean.afterPropertiesSet();
		bean = hessianProxyFactoryBean.getObject();
		method = bean.getClass().getMethod(methodName, classes);

		return Boolean.TRUE;
	}

	@Override
    public Object invoke(String args) throws Exception {
		Class[] classes = ClassUtils.forNames(hessianServiceConfig.getArguments());

		List list = null;
		if (!StringUtils.hasText(args)) {
			list = new ArrayList();
		} else if (JsonUtils.isObject(args)) {
			list = new ArrayList();
			list.add(JsonUtils.readValue(args, classes[0]));
		} else {
			list = JsonUtils.readValue(args, List.class);
		}

		return method.invoke(bean, list.toArray());
	}

}
