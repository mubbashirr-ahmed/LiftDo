package com.example.liftdo.Model;

public class CommissionModel {
    String price, date;
    public CommissionModel(String price, String date) {
        this.date = date;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
