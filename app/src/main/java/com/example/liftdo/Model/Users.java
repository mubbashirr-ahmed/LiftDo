package com.example.liftdo.Model;

public class Users {
    String token, ID, email, username, usertype, password, phNo, CNIC;
    int warnings;
    String vehicleNum, vehicleModel, vehicleType, licenseNum, status;

    public Users() {
    }

    public Users(String id, String email, String username, String password, String phoneNum, String cnic, String userType) {
        this.email = email;
        this.username = username;
        this.usertype = userType;
        this.password = password;
        this.phNo = phoneNum;
        this.CNIC = cnic;
        this.ID = id;
    }
    public Users(String token, String email, String username, String usertype, String password, String phNo, String CNIC, String ID) {
        this.token = token;
        this.email = email;
        this.username = username;
        this.usertype = usertype;
        this.password = password;
        this.phNo = phNo;
        this.CNIC = CNIC;
        this.ID = ID;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }


}
