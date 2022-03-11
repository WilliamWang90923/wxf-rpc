package com.wxf.wxfrpc.provider.spring;

import com.wxf.wxfrpc.provider.annotation.WxfRpcService;
import com.wxf.wxfrpc.provider.config.ServerConfig;
import com.wxf.wxfrpc.provider.config.ServiceConfig;
import com.wxf.wxfrpc.provider.config.WxfRpcProviderContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WxfRpcProviderPostProcessor implements ApplicationContextAware, InstantiationAwareBeanPostProcessor {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(WxfRpcService.class)) {
            ServiceConfig serverConfig = new ServiceConfig();
            serverConfig.setReference(bean);
            serverConfig.setService(bean.getClass().getInterfaces()[0]);
            System.out.println("A network-based service discovered: " + serverConfig);
            WxfRpcProviderContext.saveServiceConfig(serverConfig);
        }
        return true;
    }
}
