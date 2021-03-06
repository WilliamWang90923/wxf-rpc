package com.wxf;

import com.wxf.wxfrpc.common.serialize.json.JsonSerialization;
import com.wxf.wxfrpc.provider.server.RpcRequest;
import com.wxf.wxfrpc.provider.server.RpcResponse;

import java.io.IOException;
import java.net.Socket;

public class SocketTests {

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("127.0.0.1", 8081);
        RpcRequest request = new RpcRequest();
        request.setClassName("com.wxf.rpc.api.SmsService");
        request.setMethodName("send");
        request.setArgs(new Object[]{"13866188370", "I am mom."});
        request.setParameterTypes(new Class[]{String.class, String.class});

        client.getOutputStream().write(new JsonSerialization().serialize(request));

        byte[] response = new byte[1024];
        client.getInputStream().read(response);

        RpcResponse deserialize = (RpcResponse) new JsonSerialization().deserialize(response, RpcResponse.class);
        System.out.println(deserialize);
        client.close();
    }
}
