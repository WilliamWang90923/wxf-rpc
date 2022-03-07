package com.wxf.rpc.api;

public interface SmsService {

    Object send(String phone, String content);
}
