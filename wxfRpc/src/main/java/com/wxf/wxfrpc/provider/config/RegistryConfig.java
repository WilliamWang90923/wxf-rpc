package com.wxf.wxfrpc.provider.config;

public class RegistryConfig {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RegistryConfig{" +
                "address='" + address + '\'' +
                '}';
    }
}
