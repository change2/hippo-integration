package com.change.hippo.utils.ri.multithread;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import com.change.hippo.utils.ri.RequestIdentityHolder;

public class ProxyAdapter extends AbstractAdapter implements MethodInterceptor {

	public ProxyAdapter(){
		super();
	}

    public ProxyAdapter(boolean autoCreator){
        super();
        if (autoCreator){
            this.requestInfo = RequestIdentityHolder.generateNew();
            RequestIdentityHolder.set(requestInfo);
        }
    }
	
	@Override
	public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		super.supportRequestIdentity();
		return methodProxy.invokeSuper(proxy, params);
	}

}

