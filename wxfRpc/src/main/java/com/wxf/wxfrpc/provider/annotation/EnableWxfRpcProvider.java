package com.wxf.wxfrpc.provider.annotation;


import com.wxf.wxfrpc.provider.server.NettyProviderServer;
import com.wxf.wxfrpc.provider.spring.WxfRpcConfiguration;
import com.wxf.wxfrpc.provider.spring.WxfRpcProviderPostProcessor;
import com.wxf.wxfrpc.registry.ProviderRegistStart;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({NettyProviderServer.class, WxfRpcConfiguration.class,
        WxfRpcProviderPostProcessor.class, ProviderRegistStart.class})
public @interface EnableWxfRpcProvider {
}
