package com.wxf.rpc;

import com.wxf.wxfrpc.provider.annotation.EnableWxfRpcProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

//@Configuration
@ComponentScan("com.wxf.rpc")
@PropertySource("classpath:/wxf-rpc.yml")
@EnableWxfRpcProvider
public class SmsApplication {

	public static void main(String[] args) throws IOException {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SmsApplication.class);
		context.start();

		System.in.read();
		context.close();
	}

}
