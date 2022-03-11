package com.wxf.wxfrpc.provider.config;

import java.util.concurrent.ConcurrentHashMap;

public class WxfRpcProviderContext {

    public static ConcurrentHashMap<String, ServiceConfig> serviceMap = new ConcurrentHashMap<>();

    public static void saveServiceConfig(ServiceConfig config) {
        serviceMap.putIfAbsent(config.getService().getName(), config);
    }
}
