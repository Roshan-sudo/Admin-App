package com.ace.services.one.adminapp.models;

public class AdminModel {
    private String adminId, name, phoneNo;

    public AdminModel() {
        // Required Empty Constructor
    }

    public AdminModel(String adminId, String name, String phoneNo) {
        this.adminId = adminId;
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
