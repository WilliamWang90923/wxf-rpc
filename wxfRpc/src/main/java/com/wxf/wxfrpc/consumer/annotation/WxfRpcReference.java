package com.wxf.wxfrpc.consumer.annotation;


import com.wxf.wxfrpc.consumer.spring.WxfRRpcConsumerPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Import({WxfRRpcConsumerPostProcessor.class})
public @interface WxfRpcReference {
}
