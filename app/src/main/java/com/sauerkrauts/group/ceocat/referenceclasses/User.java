package com.sauerkrauts.group.ceocat.referenceclasses;

public class User {
    private String userId;
    private String email;
    private String fullname;
    private String userType;
    private String department;
    private String warehouse;

    // Constructors
    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String email, String fullname, String userType) {
        this.userId = userId;
        this.email = email;
        this.fullname = fullname;
        this.userType = userType;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
}

