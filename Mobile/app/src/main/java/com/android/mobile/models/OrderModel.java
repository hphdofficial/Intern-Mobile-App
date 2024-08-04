package com.android.mobile.models;

import java.time.LocalDateTime;

public class OrderModel {
    private int id;
    private int member_id;
    private String txn_ref;
    private String amount;
    private String order_info;
    private String response_code;
    private String transaction_no;
    private String bank_code;
    private String pay_date;
    private String status;
    private String created_at;
    private String updated_at;

    public OrderModel() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getTxn_ref() {
        return txn_ref;
    }

    public void setTxn_ref(String txn_ref) {
        this.txn_ref = txn_ref;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder_info() {
        return order_info;
    }

    public void setOrder_info(String order_info) {
        this.order_info = order_info;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}