package com.example.liftdo.NewVersion.Model;

public class PutRequest {


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public double getPickPointLat() {
        return pickPointLat;
    }

    public void setPickPointLat(double pickPointLat) {
        this.pickPointLat = pickPointLat;
    }

    public double getPickPointLong() {
        return pickPointLong;
    }

    public void setPickPointLong(double pickPointLong) {
        this.pickPointLong = pickPointLong;
    }

    public double getDropPointLat() {
        return dropPointLat;
    }

    public void setDropPointLat(double dropPointLat) {
        this.dropPointLat = dropPointLat;
    }

    public double getDropPointLong() {
        return dropPointLong;
    }

    public void setDropPointLong(double dropPointLong) {
        this.dropPointLong = dropPointLong;
    }

    String UID;
    String name;
    String phno;
    String vType;

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    String vName;

    String addressLineP, addressLineD;
    double pickPointLat, pickPointLong, dropPointLat, dropPointLong;
    public String getAddressLineP() {
        return addressLineP;
    }

    public void setAddressLineP(String addressLineP) {
        this.addressLineP = addressLineP;
    }

    public String getAddressLineD() {
        return addressLineD;
    }

    public void setAddressLineD(String addressLineD) {
        this.addressLineD = addressLineD;
    }


    public PutRequest(){

    }


}
