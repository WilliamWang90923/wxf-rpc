package com.wxf.wxfrpc.provider.config;

public class ServiceConfig {

    private Class service;

    private Object reference;

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "service=" + service +
                ", reference=" + reference +
                '}';
    }
}
