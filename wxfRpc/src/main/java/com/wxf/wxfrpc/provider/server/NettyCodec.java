package com.wxf.wxfrpc.provider.server;

import com.wxf.wxfrpc.common.tools.ByteUtils;
import com.wxf.wxfrpc.common.serialize.json.JsonSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NettyCodec extends ChannelDuplexHandler {

    Class decodeType;
    final static byte[] MAGIC = new byte[]{(byte) 0xda, (byte) 0xbb};

    public NettyCodec(Class decodeType) {
        this.decodeType = decodeType;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        List<Object> out = decode(in);

        for (Object o : out) {
            ctx.fireChannelRead(o);
        }

    }

    ByteBuf msgCache = Unpooled.buffer();

    private List<Object> decode(ByteBuf in) throws IOException {
        ArrayList<Object> out = new ArrayList<>();

        // merge
        msgCache.writeBytes(in);

        // parse HEADER
        while (true) {
            if (msgCache.readableBytes() < 6) {
                byte[] data = new byte[msgCache.readableBytes()];
                msgCache.readBytes(data);
                msgCache.clear();
                msgCache.writeBytes(data);
                return out;
            }
            // 1. find MAGIC, length
            byte[] magic = new byte[2];
            msgCache.readBytes(magic);
            while (true) {
                if (magic[0] != MAGIC[0] || magic[1] != MAGIC[1]) {
                    if (msgCache.readableBytes() == 0) {
                        msgCache.clear();
                        // try next data and this one could form a MAGIC
                        msgCache.writeByte(magic[1]);
                        return out;
                    }
                    magic[0] = magic[1];
                    magic[1] = msgCache.readByte();
                } else {
                    break;
                }
            }
            byte[] lenBytesRead = new byte[4];
            msgCache.readBytes(lenBytesRead);
            int length = ByteUtils.Bytes2Int_BE(lenBytesRead);

            // body
            if (msgCache.readableBytes() < length) {
                byte[] data = new byte[msgCache.readableBytes()];
                msgCache.readBytes(data);
                msgCache.clear();
                msgCache.writeBytes(magic);
                msgCache.writeBytes(lenBytesRead);
                msgCache.writeBytes(data);
                return out;
            }
            byte[] body = new byte[length];
            msgCache.readBytes(body);

            Object o = new JsonSerialization().deserialize(body, decodeType);
            out.add(o);
        }

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        byte[] body = new JsonSerialization().serialize(msg);
        // 2. header
        ByteBuf requestBuffer = Unpooled.buffer();
        requestBuffer.writeByte(MAGIC[0]);
        requestBuffer.writeByte(MAGIC[1]);
        // 3. length
        int len = body.length;
        byte[] lenBytes = ByteUtils.int2bytes(len);
        requestBuffer.writeBytes(lenBytes);
        requestBuffer.writeBytes(body);

        super.write(ctx, requestBuffer, promise);
    }
}
