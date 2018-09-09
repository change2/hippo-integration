package com.change.hippo.utils.ri.multithread;

import net.sf.cglib.proxy.Enhancer;

public final class CglibAdapterFactory {

    private CglibAdapterFactory() {
    }

    public static <T> T adapt(Class<T> clazz, boolean autoCreator) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);// 设置代理目标
        enhancer.setCallback(new ProxyAdapter(autoCreator));// 设置回调
        enhancer.setClassLoader(clazz.getClassLoader());
        return (T) enhancer.create();
    }
}
