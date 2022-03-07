package com.wxf.rpc.sms;


import com.wxf.wxfrpc.provider.annotation.WxfRpcService;
import com.wxf.rpc.api.SmsService;

@WxfRpcService
public class SmsServiceImpl implements SmsService {

    @Override
    public Object send(String phone, String content) {
        System.out.println("wxf-rpc--Send Msg: " + phone + " : " + content);
        return "rpc send msg success!";
    }
}
