package com.wxf.wxfrpc.consumer.spring;

import com.wxf.wxfrpc.consumer.annotation.WxfRpcReference;
import com.wxf.wxfrpc.consumer.proxy.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WxfRRpcConsumerPostProcessor implements ApplicationContextAware, InstantiationAwareBeanPostProcessor {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

        for (var field : bean.getClass().getDeclaredFields()) {
            try {
                if (!field.isAnnotationPresent(WxfRpcReference.class)) {
                    continue;
                }
                WxfRpcReference wxfRpcReference = field.getAnnotation(WxfRpcReference.class);

                Object referenceBean = ProxyFactory.getProxy(new Class[]{field.getType()});
                field.setAccessible(true);
                field.set(bean, referenceBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
