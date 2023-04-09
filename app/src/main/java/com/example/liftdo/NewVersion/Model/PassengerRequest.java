package com.example.liftdo.NewVersion.Model;

public class PassengerRequest {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getAddressLineD() {
        return addressLineD;
    }

    public void setAddressLineD(String addressLineD) {
        this.addressLineD = addressLineD;
    }

    public double getpLat() {
        return pLat;
    }

    public void setpLat(double pLat) {
        this.pLat = pLat;
    }

    public double getpLong() {
        return pLong;
    }

    public void setpLong(double pLong) {
        this.pLong = pLong;
    }

    public double getdLat() {
        return dLat;
    }

    public void setdLat(double dLat) {
        this.dLat = dLat;
    }

    public double getdLong() {
        return dLong;
    }

    public void setdLong(double dLong) {
        this.dLong = dLong;
    }
    public PassengerRequest(){}
    String name;
    String ID;
    String phNo;
    String vType;
    String addressLineD;

    public String getAddressLineP() {
        return addressLineP;
    }

    public void setAddressLineP(String addressLineP) {
        this.addressLineP = addressLineP;
    }

    String addressLineP;
    double pLat, pLong, dLat, dLong, distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


}
