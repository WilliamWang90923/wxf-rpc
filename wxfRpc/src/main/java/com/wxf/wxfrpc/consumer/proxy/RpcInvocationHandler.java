package com.wxf.wxfrpc.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("I am a proxy, I call :" + method.getName());
        return null;
    }
}
