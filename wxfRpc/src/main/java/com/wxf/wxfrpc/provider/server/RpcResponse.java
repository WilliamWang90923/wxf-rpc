package com.wxf.wxfrpc.provider.server;

public class RpcResponse {

    private int status;
    private Object content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "status=" + status +
                ", content=" + content +
                '}';
    }
}