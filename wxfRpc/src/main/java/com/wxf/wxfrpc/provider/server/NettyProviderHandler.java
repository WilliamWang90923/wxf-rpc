package com.wxf.wxfrpc.provider.server;

import com.wxf.wxfrpc.common.serialize.json.JsonSerialization;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class NettyProviderHandler extends SimpleChannelInboundHandler {

    ApplicationContext applicationContext;

    public NettyProviderHandler(ApplicationContext applicationContext) {
        super();
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcRequest rpcRequest = (RpcRequest) msg;
        String className = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethodName();
        Object[] args = rpcRequest.getArgs();
        Class[] parameterTypes = rpcRequest.getParameterTypes();

        Class<?> serviceClazz = Class.forName(className);
        Object serviceBean = applicationContext.getBean(serviceClazz);
        Method method = serviceClazz.getMethod(methodName, parameterTypes);
        Object res = method.invoke(serviceBean, args);

        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        response.setStatus(200);
        response.setContent(res);

         // byte[] serialize = new JsonSerialization().serialize(response);
        ctx.writeAndFlush(response);

    }
}
