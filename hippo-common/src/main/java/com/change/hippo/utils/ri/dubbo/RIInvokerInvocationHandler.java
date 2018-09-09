package com.change.hippo.utils.ri.dubbo;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcInvocation;

import com.change.hippo.utils.ri.RequestIdentityHolder;
import com.change.hippo.utils.ri.RequestInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 支持RequestIdentity组件的Dubbo调用处理器
 * 
 * @date 2015-7-27
 */
public class RIInvokerInvocationHandler implements InvocationHandler {

    private final Invoker<?> invoker;
    
    public RIInvokerInvocationHandler(Invoker<?> handler){
        this.invoker = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }
        RpcInvocation rpcInvocation = new RpcInvocation(method, args);
        supportRequestIdentity(rpcInvocation);
        return invoker.invoke(rpcInvocation).recreate();
    }
    
    /**
     * 在RPC调用中添加RequestIdentity组件支持
     * @param invocation RPC调用
     */
    public static void supportRequestIdentity(RpcInvocation invocation){
    	RequestInfo requestInfo = RequestIdentityHolder.get();
    	if(requestInfo != null){
    		invocation.setAttachment("rid", requestInfo.getId());
    		invocation.setAttachmentIfAbsent("rip", NetUtils.getLocalAddress().getHostAddress());
    	}
    }

}