package org.change.hippo.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Map;


public class SpringContextUtils implements BeanFactoryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextUtils.class);
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringContextUtils.beanFactory = configurableListableBeanFactory;
    }

	public static Object getBeanById(String id) {
		return beanFactory.getBean(id);
	}

	public static Object getBeanByType(String type) {
		Object object = null;
		try {
			object = getBeanByType(Class.forName(type));
		} catch (Exception e) {
            logger.error(e.getMessage(),e);
		}
		return object;
	}

	public static <T> T getBeanByType(Class<T> clazz) {
		return beanFactory.getBean(clazz);
	}

	public static Object getDirectImplBean(String type) {
		Object object = null;
		try {
			object = getDirectImplBean(Class.forName(type));
		} catch (Exception e) {
            logger.error(e.getMessage(),e);
		}
		return object;
	}

	public static <T> T getDirectImplBean(Class<T> clazz) {
		String type = getStereoType(clazz);

		if (!"interface".equals(type)) {
			throw new RuntimeException("Type " + type + " is not support.");
		}

		Map<String, T> map = beanFactory.getBeansOfType(clazz);

		if (map == null || map.isEmpty()) {
			return null;
		}

		if (map.size() == 1) {
			return map.values().iterator().next();
		}

		T r = null;
		int count = 0;

		for (T t : map.values()) {
			Class[] interfaces = t.getClass().getInterfaces();
			for (Class i : interfaces) {
				if (i == clazz) {
					r = t;
					count++;
					break;
				}
			}
		}

		if (count > 1) {
			throw new RuntimeException("expect 1, but " + count + " beans found that are directly implement " + clazz + ".");
		}

		return r;
	}

	public static String getStereoType(Class<?> clazz) {
		return clazz.isInterface() ? "interface" : (clazz.isPrimitive() ? "primitive" : "class");
	}

	public static <T> Map<String, T> getBeans(Class<T> type){
        return beanFactory.getBeansOfType(type);
    }
}
