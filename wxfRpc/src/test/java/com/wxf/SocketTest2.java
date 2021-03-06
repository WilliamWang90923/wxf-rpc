package com.wxf;

import com.wxf.wxfrpc.common.serialize.json.JsonSerialization;
import com.wxf.wxfrpc.provider.server.RpcRequest;

import java.io.IOException;
import java.net.Socket;

public class SocketTest2 {

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("127.0.0.1", 8081);
        RpcRequest request = new RpcRequest();
        request.setClassName("com.wxf.rpc.api.SmsService");
        request.setMethodName("send");
        request.setArgs(new Object[]{"13866188370", "I am mom."});
        request.setParameterTypes(new Class[]{String.class, String.class});


        for (int i=0; i<10; i++) {
            client.getOutputStream().write(new JsonSerialization().serialize(request));
        }
    }
}
