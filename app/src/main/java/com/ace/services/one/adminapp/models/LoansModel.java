package com.ace.services.one.adminapp.models;

public class LoansModel {
    private String loanId;
    private int loanTenure;
    private float loanAmount;
    private boolean isApproved;

    public LoansModel() {
        // Required Empty Constructor
    }

    public LoansModel(String loanId, int loanTenure, float loanAmount, boolean isApproved) {
        this.loanId = loanId;
        this.loanTenure = loanTenure;
        this.loanAmount = loanAmount;
        this.isApproved = isApproved;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }

    public float getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(float loanAmount) {
        this.loanAmount = loanAmount;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
