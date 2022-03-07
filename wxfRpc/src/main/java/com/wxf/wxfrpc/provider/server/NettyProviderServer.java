package com.wxf.wxfrpc.provider.server;

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


public class NettyProviderServer implements SmartApplicationListener, ApplicationContextAware {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == ContextClosedEvent.class || eventType == ContextStartedEvent.class;
    }

    ApplicationContext context;
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        try {
            if (event instanceof ContextStartedEvent) {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap
                        .group(boss, worker)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress("127.0.0.1", 8081));

                bootstrap.childHandler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new NettyCodec(RpcRequest.class));
                                ch.pipeline().addLast(new NettyProviderHandler(context));
                            }
                        }
                );
                bootstrap.bind().sync();
                System.out.println("Port binding and server start complete!");
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
