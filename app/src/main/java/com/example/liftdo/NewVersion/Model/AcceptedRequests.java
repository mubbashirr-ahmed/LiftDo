package com.example.liftdo.NewVersion.Model;

public class AcceptedRequests {
    public AcceptedRequests(){

    }
    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPickPoint() {
        return pickPoint;
    }

    public void setPickPoint(String pickPoint) {
        this.pickPoint = pickPoint;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getDriverPhno() {
        return driverPhno;
    }

    public void setDriverPhno(String driverPhno) {
        this.driverPhno = driverPhno;
    }

    public String getPassengerPhno() {
        return passengerPhno;
    }

    public void setPassengerPhno(String passengerPhno) {
        this.passengerPhno = passengerPhno;
    }

    public AcceptedRequests(String driverID, String passengerID, String vehicleNum, String driverName, String pickPoint, String dropPoint, String driverPhno, String passengerPhno,  double lat, double lon) {
        this.driverID = driverID;
        this.passengerID = passengerID;
        this.vehicleNum = vehicleNum;
        this.driverName = driverName;
        this.pickPoint = pickPoint;
        this.dropPoint = dropPoint;
        this.driverPhno = driverPhno;
        this.passengerPhno = passengerPhno;
        this.lat = lat;
        this.lon = lon;
    }

    String driverID, passengerID, vehicleNum, driverName, pickPoint, dropPoint, driverPhno, passengerPhno;
    double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    double lon;
}
