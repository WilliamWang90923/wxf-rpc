package com.wxf.rpc.order;

import com.wxf.myrpc.consumer.annotation.WxfRpcReference;
import com.wxf.rpc.OrderService;
import com.wxf.rpc.SmsService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @WxfRpcReference
    SmsService smsService;

    @Override
    public void create(String content) {
        System.out.println("New Order Constructed!: " + content);
        Object smsRes = smsService.send("10086", "You have a new wife!");
        System.out.println("smsService call result: " + smsRes);
    }
}
