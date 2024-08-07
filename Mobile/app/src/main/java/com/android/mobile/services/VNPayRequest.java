package com.android.mobile.services;

public class VNPayRequest {
    private int amount;
    private String orderInfo;
    private String orderId;
    private String ipAddr;

    private VNPayRequest(Builder builder) {
        this.amount = builder.amount;
        this.orderInfo = builder.orderInfo;
        this.orderId = builder.orderId;
        this.ipAddr = builder.ipAddr;
    }

    public int getAmount() {
        return amount;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public static class Builder {
        private int amount;
        private String orderInfo;
        private String orderId;
        private String ipAddr;

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder setOrderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
            return this;
        }

        public Builder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setIpAddr(String ipAddr) {
            this.ipAddr = ipAddr;
            return this;
        }

        public VNPayRequest build() {
            return new VNPayRequest(this);
        }
    }
}