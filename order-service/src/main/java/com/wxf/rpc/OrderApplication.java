package com.wxf.rpc;

import com.wxf.rpc.api.OrderService;
import com.wxf.wxfrpc.consumer.annotation.EnableWxfRpcConsumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@ComponentScan("com.wxf.rpc")
@PropertySource("classpath:/wxf-rpc.properties")
@EnableWxfRpcConsumer
public class OrderApplication {

	public static void main(String[] args) throws IOException {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OrderApplication.class);
		context.start();

		OrderService orderService = context.getBean(OrderService.class);
		orderService.create("buy a love machine.");

		System.in.read();
		context.close();
	}

}
