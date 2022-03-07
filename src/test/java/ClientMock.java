import com.wxf.myrpc.common.serialize.json.JsonSerialization;
import com.wxf.myrpc.common.tools.ByteUtils;
import com.wxf.myrpc.provider.server.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.net.Socket;

public class ClientMock {

    public static void main(String[] args) throws IOException {
        // 1. body
        RpcRequest request = new RpcRequest();
        request.setClassName("com.wxf.rpc.SmsService");
        request.setMethodName("send");
        request.setArgs(new Object[]{"13866188370", "I am mom."});
        request.setParameterTypes(new Class[]{String.class, String.class});
        byte[] body = new JsonSerialization().serialize(request);

        // 2. header
        ByteBuf requestBuffer = Unpooled.buffer();
        requestBuffer.writeByte(0xda);
        requestBuffer.writeByte(0xbb);
        // 3. length
        int len = body.length;
        byte[] lenBytes = ByteUtils.int2bytes(len);
        requestBuffer.writeBytes(lenBytes);
        System.out.println(new String(new byte[]{(byte) 0xda, (byte) 0xdd}));
    }
}
