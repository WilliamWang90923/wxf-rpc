package com.wxf.rpc;

public interface SmsService {

    Object send(String phone, String content);
}
