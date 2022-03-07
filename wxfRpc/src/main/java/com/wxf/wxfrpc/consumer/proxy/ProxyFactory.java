package com.wxf.wxfrpc.consumer.proxy;


import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static Object getProxy(Class<?> [] intfs) {
        return Proxy.newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                intfs,
                new RpcInvocationHandler()
        );
    }
}
