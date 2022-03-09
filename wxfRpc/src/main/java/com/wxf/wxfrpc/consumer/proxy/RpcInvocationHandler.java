package com.wxf.wxfrpc.consumer.proxy;

import com.wxf.wxfrpc.consumer.client.NettyConsumer;
import com.wxf.wxfrpc.consumer.client.RpcConnection;
import com.wxf.wxfrpc.provider.server.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("toString")) {
            return "toString method ignored";
        }
        // 1. construct a rpcRequest instance
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setArgs(args);
        System.out.println("client ready to launch rpc call: " + rpcRequest);

        // 2. send data by socket
        RpcConnection connection = NettyConsumer.connect("127.0.0.1", 8081);
        return connection.write(rpcRequest);
    }
}
