package com.wxf.wxfrpc.provider.server;

public class RpcResponse {

    private String requestId;
    private int status;
    private Object content;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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
                "requestId=" + requestId +
                ", status=" + status +
                ", content=" + content +
                '}';
    }
}
