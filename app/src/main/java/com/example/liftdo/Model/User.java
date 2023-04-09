package com.example.liftdo.Model;

public abstract class User {
    private String id;
    private String email;
    private String username;
    private String password;
    private long phoneNum;
    private long cnic;
    private String userType;

    public User(){

    }

    public User(String id, String email, String username, String password, long phoneNum, long cnic, String userType) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
        this.cnic = cnic;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getPhoneNum() {
        return phoneNum;
    }

    public long getCnic() {
        return cnic;
    }

    public String getUserType() {
        return userType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setCnic(long cnic) {
        this.cnic = cnic;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
