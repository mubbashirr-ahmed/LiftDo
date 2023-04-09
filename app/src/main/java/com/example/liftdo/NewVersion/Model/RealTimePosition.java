package com.example.liftdo.NewVersion.Model;


public class RealTimePosition {



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isReach() {
        return reach;
    }

    public void setReach(boolean reach) {
        this.reach = reach;
    }

    double latitude;
    double longitude;
    boolean reach;
    public  RealTimePosition(){
    }
}
