package com.wxf.wxfrpc.provider.spring;

import com.wxf.wxfrpc.provider.config.RegistryConfig;
import com.wxf.wxfrpc.provider.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;

public class WxfRpcConfiguration implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    Environment environment;
//
//    public WxfRpcConfiguration(Environment environment) {
//        this.environment = environment;
//    }
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry, BeanNameGenerator generator) {
        BeanDefinitionBuilder builder;

        builder = BeanDefinitionBuilder.genericBeanDefinition(ServerConfig.class);
        for (Field field : ServerConfig.class.getDeclaredFields()) {
            String value = environment.getProperty("wxfrpc.server." + field.getName());
            builder.addPropertyValue(field.getName(), value);
        }
        registry.registerBeanDefinition("serverConfig", builder.getBeanDefinition());

        builder = BeanDefinitionBuilder.genericBeanDefinition(RegistryConfig.class);
        for (Field field : RegistryConfig.class.getDeclaredFields()) {
            String value = environment.getProperty("wxfrpc.registry." + field.getName());
            builder.addPropertyValue(field.getName(), value);
        }
        registry.registerBeanDefinition("registryConfig", builder.getBeanDefinition());
    }
}
