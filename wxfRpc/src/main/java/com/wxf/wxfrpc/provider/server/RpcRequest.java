package com.wxf.wxfrpc.provider.server;

import java.util.Arrays;
import java.util.UUID;

public class RpcRequest {

    private String requestId;
    private String className;
    private String methodName;
    private Object[] args;
    private Class[] parameterTypes;

    public RpcRequest() {
        this.requestId = UUID.randomUUID().toString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId=" + requestId +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
