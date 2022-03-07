package com.wxf.rpc.order;

import com.wxf.wxfrpc.consumer.annotation.WxfRpcReference;
import com.wxf.wxfrpc.provider.annotation.WxfRpcService;
import com.wxf.rpc.api.OrderService;
import com.wxf.rpc.api.SmsService;

@WxfRpcService
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
