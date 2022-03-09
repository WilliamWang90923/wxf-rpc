package com.wxf.wxfrpc.consumer.client;

import com.wxf.wxfrpc.provider.server.NettyCodec;
import com.wxf.wxfrpc.provider.server.RpcRequest;
import com.wxf.wxfrpc.provider.server.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class NettyConsumer {

    public static ConcurrentHashMap<InetSocketAddress, RpcConnection> connections = new ConcurrentHashMap<>();

    public static RpcConnection connect(String host, int port) throws InterruptedException {

        InetSocketAddress providerAddress = new InetSocketAddress(host, port);
        RpcConnection connExist = connections.get(providerAddress);

        if (connExist != null)
            return connExist;

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new NettyCodec(RpcResponse.class));
                ch.pipeline().addLast(new NettyConsumerHandler());
            }
        });

        Channel channel = bootstrap.connect(providerAddress).await().channel();
        RpcConnection rpcConnection = new RpcConnection(channel);

        if (connections.putIfAbsent(providerAddress, rpcConnection) != null) {
            channel.closeFuture();
            return connections.get(providerAddress);
        }
        return rpcConnection;
    }
}
