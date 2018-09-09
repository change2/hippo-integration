package org.change.hippo.server.service.soap;

import org.change.hippo.server.util.ClassUtils;
import org.change.hippo.server.util.JsonUtils;
import org.change.hippo.server.model.soap.SoapServiceConfig;
import org.change.hippo.server.service.AbstractService;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SoapService extends AbstractService<SoapServiceConfig> {

	private SoapServiceConfig serviceConfig;
	private Method method;
	private Object bean;

	@Override
    public boolean init(SoapServiceConfig serviceConfig) throws Exception {
		this.serviceConfig = serviceConfig;
        setServiceId(serviceConfig.getServiceId());
        setServiceConfig(serviceConfig);

		String methodName = serviceConfig.getMethod();
		Class interfaze = Class.forName(serviceConfig.getInterfaze());
		Class[] classes = ClassUtils.forNames(serviceConfig.getArguments());

		JaxWsPortProxyFactoryBean jaxWsPortProxyFactoryBean = new JaxWsPortProxyFactoryBean();
		jaxWsPortProxyFactoryBean.setServiceInterface(interfaze);
		jaxWsPortProxyFactoryBean.setWsdlDocumentUrl(new URL(serviceConfig.getUrl()));
		jaxWsPortProxyFactoryBean.afterPropertiesSet();

		bean = jaxWsPortProxyFactoryBean.getObject();
		method = bean.getClass().getMethod(methodName, classes);

		return Boolean.TRUE;
	}

	public Object invoke(String args) throws Exception {
		Class[] classes = ClassUtils.forNames(serviceConfig.getArguments());

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
