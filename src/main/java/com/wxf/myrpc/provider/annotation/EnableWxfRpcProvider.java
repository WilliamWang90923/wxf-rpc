package com.wxf.myrpc.provider.annotation;


import com.wxf.myrpc.provider.server.NettyProviderServer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({NettyProviderServer.class})
public @interface EnableWxfRpcProvider {
}