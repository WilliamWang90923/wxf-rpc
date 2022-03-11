package com.wxf.wxfrpc.registry;

import com.wxf.wxfrpc.provider.config.RegistryConfig;
import com.wxf.wxfrpc.provider.config.ServerConfig;
import com.wxf.wxfrpc.provider.config.WxfRpcProviderContext;
import com.wxf.wxfrpc.provider.server.NettyCodec;
import com.wxf.wxfrpc.provider.server.NettyProviderHandler;
import com.wxf.wxfrpc.provider.server.RpcRequest;
import com.wxf.wxfrpc.registry.redis.RedisRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SmartApplicationListener;

import java.net.InetSocketAddress;
import java.net.URI;


public class ProviderRegistStart implements SmartApplicationListener, ApplicationContextAware {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == ContextStartedEvent.class;
    }

    ApplicationContext context;
    private static RedisRegistry redisRegistry = new RedisRegistry();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        try {
            RegistryConfig registryConfig = context.getBean(RegistryConfig.class);
            ServerConfig serverConfig = context.getBean(ServerConfig.class);
            redisRegistry.init(new URI(registryConfig.getAddress()));
            for (String serviceName : WxfRpcProviderContext.serviceMap.keySet()) {
                URI serviceURI = new URI("//" + serverConfig.getHost() + ":" + serverConfig.getPort()
                        +"/" + serviceName + "/");
                redisRegistry.register(serviceURI);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 9999;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
