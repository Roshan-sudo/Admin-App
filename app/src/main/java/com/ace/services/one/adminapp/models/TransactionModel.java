package com.ace.services.one.adminapp.models;

public class TransactionModel {
    private float amount;
    private String date, time, paymentMode, loanId, transactionId;
    private int status;

    public TransactionModel() {
        // Required Empty Constructor
    }

    public TransactionModel(float amount, String date, String time, String paymentMode, String loanId, String transactionId, int status) {
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.paymentMode = paymentMode;
        this.loanId = loanId;
        this.transactionId = transactionId;
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

