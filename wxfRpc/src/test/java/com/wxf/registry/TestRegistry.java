package com.wxf.registry;

import com.wxf.wxfrpc.registry.NotifyListener;
import com.wxf.wxfrpc.registry.redis.RedisRegistry;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class TestRegistry {

    public static void main(String[] args) throws URISyntaxException {
        RedisRegistry registry = new RedisRegistry();
        registry.init(new URI("redis://192.168.5.129:6379"));
        registry.register(new URI("//127.0.0.1:10088/com.wxf.rpc.api.SmsService/"));

        registry.subscribe("com.wxf.rpc.api.SmsService",
                uriSet -> System.out.println("Services info changed: " + uriSet));
    }
}
