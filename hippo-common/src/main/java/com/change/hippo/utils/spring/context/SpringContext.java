package com.change.hippo.utils.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	public static <T> T getBean(Class<T> clazz){
		return context.getBean(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		return (T) context.getBean(name);
	}

}
