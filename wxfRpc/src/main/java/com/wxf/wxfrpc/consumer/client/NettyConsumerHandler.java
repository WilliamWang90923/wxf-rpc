package com.wxf.wxfrpc.consumer.client;

import com.wxf.wxfrpc.provider.server.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyConsumerHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {

        System.out.println("Receive response: " + response);
        // dispatch result to relative Thread
        if (response.getStatus() == 99) {
            throw new Exception(response.getContent().toString());
        }

        RpcConnection connection = NettyConsumer.connections.get(ctx.channel().remoteAddress());
        connection.complete(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // close connection
        NettyConsumer.connections.remove(ctx.channel().remoteAddress());
        super.exceptionCaught(ctx, cause);
    }
}
