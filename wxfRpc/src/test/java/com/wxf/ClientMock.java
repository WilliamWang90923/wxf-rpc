package com.wxf;

import com.wxf.wxfrpc.common.serialize.json.JsonSerialization;
import com.wxf.wxfrpc.common.tools.ByteUtils;
import com.wxf.wxfrpc.provider.server.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.Socket;

public class ClientMock {

    final static byte[] MAGIC = new byte[]{(byte) 0xda, (byte) 0xbb};

    public static void main(String[] args) throws Exception {
        // 1. body
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("com.wxf.rpc.api.SmsService");
        rpcRequest.setMethodName("send");
        rpcRequest.setParameterTypes(new Class[]{String.class, String.class});
        rpcRequest.setArgs(new Object[]{"13800138000", "iamtony"});
        byte[] body = new JsonSerialization().serialize(rpcRequest);
        System.out.println(body.length + " - request-body:" + new String(body));
        // build request
        // 2. header
        ByteBuf requestBuffer = Unpooled.buffer();
        requestBuffer.writeByte(MAGIC[0]);
        requestBuffer.writeByte(MAGIC[1]);
        // 3. length
        int len = body.length;
        byte[] lenBytes = ByteUtils.int2bytes(len);
        requestBuffer.writeBytes(lenBytes);
        // 4. body
        requestBuffer.writeBytes(body);
        System.out.println("request length:" + requestBuffer.readableBytes());

        // client
        Socket client = new Socket("127.0.0.1", 8081);
        byte[] req = new byte[requestBuffer.readableBytes()];
        requestBuffer.readBytes(req);

        for (int i = 0; i < 20; i++) {
            client.getOutputStream().write(req);
        }

        System.in.read();
    }
}
