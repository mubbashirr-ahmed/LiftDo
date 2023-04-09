package com.example.liftdo.NewVersion.Model;

public class Request {
    private long requestId;
    private Passenger passenger;
    private double pickPointLongitude;
    private double pickPointLatitude;
    private double dropPointLongitude;
    private double dropPointLatitude;
    private double totalDistance;
    private String city;
    private String area;

    public Request() {
    }

    public Request(long requestId, Passenger passenger, double pickPointLongitude, double pickPointLatitude,
                   double dropPointLongitude, double dropPointLatitude, double totalDistance, String city, String area) {
        this.requestId = requestId;
        this.passenger = passenger;
        this.pickPointLongitude = pickPointLongitude;
        this.pickPointLatitude = pickPointLatitude;
        this.dropPointLongitude = dropPointLongitude;
        this.dropPointLatitude = dropPointLatitude;
        this.totalDistance = totalDistance;
        this.city = city;
        this.area = area;
    }

    public long getRequestId() {
        return requestId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public double getPickPointLongitude() {
        return pickPointLongitude;
    }

    public double getPickPointLatitude() {
        return pickPointLatitude;
    }

    public double getDropPointLongitude() {
        return dropPointLongitude;
    }

    public double getDropPointLatitude() {
        return dropPointLatitude;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }


    public void setPickPointLongitude(double pickPointLongitude) {
        this.pickPointLongitude = pickPointLongitude;
    }

    public void setPickPointLatitude(double pickPointLatitude) {
        this.pickPointLatitude = pickPointLatitude;
    }

    public void setDropPointLongitude(double dropPointLongitude) {
        this.dropPointLongitude = dropPointLongitude;
    }

    public void setDropPointLatitude(double dropPointLatitude) {
        this.dropPointLatitude = dropPointLatitude;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
