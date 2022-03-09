package com.wxf.wxfrpc.consumer.client;


import com.wxf.wxfrpc.provider.server.RpcRequest;
import com.wxf.wxfrpc.provider.server.RpcResponse;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class RpcConnection {

    final static ConcurrentHashMap<String, CompletableFuture> futureMap = new ConcurrentHashMap<>();
    Channel channel;

    public RpcConnection(Channel channel) {
        this.channel = channel;
    }

    public Object write(RpcRequest request) throws Exception {

        CompletableFuture future = new CompletableFuture();
        futureMap.putIfAbsent(request.getRequestId(), future);
        channel.writeAndFlush(request);
        return future.get();
    }

    public void complete(RpcResponse response) {
        futureMap.get(response.getRequestId()).complete(response.getContent());
        futureMap.remove(response.getRequestId());
    }
}
