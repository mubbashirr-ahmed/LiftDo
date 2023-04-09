package com.example.liftdo.Model;

public class RiderInfo {
    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getLicenceNum() {
        return licenceNum;
    }

    public void setLicenceNum(String licenceNum) {
        this.licenceNum = licenceNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public RiderInfo(String vehicleNum, String licenceNum, String model, String vType, int warnings) {
        this.vehicleNum = vehicleNum;
        this.licenceNum = licenceNum;
        this.model = model;
        this.vType = vType;
        this.warnings = warnings;
    }

    String vehicleNum, licenceNum, model, vType;
    int warnings;
}
