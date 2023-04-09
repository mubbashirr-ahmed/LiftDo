package com.example.liftdo.AllActivities.Cash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftdo.AllActivities.Finish.RideComplete;
import com.example.liftdo.databinding.ActivityBillingBinding;

import org.json.JSONObject;

public class Billing extends AppCompatActivity {

    private double distance;
    private double pricePerKm = 10.0; //default value is 10 in case there is error in API
    private int vehicle_type;
    private long riderId;
    ActivityBillingBinding binding;
    String getVehicleTypeUrl = "http://192.168.173.253:8081/rider/";
    String getPriceUrl = "http://192.168.173.253:8081/price/get/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // init();
    }

    private void init(){
        distance = getIntent().getDoubleExtra("distance",5);
        riderId= getIntent().getLongExtra("riderId",0);
        setVehicleType();
        getPerKmPrice();
        clickListeners();
    }

    private void setVehicleType(){
        // here we will get the vehicle type using rider id
        String url = getVehicleTypeUrl + riderId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject riderDetailsJSON = new JSONObject(response);
                JSONObject vehicleJSON = riderDetailsJSON.getJSONObject("vehicleId");
                vehicle_type = vehicleJSON.getInt("vehicleType");
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        },error -> {});
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void getPerKmPrice() {
        String url = getPriceUrl + vehicle_type;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject priceObject = new JSONObject(response);
                pricePerKm = priceObject.getDouble("price");
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        },error -> {});
        Volley.newRequestQueue(this).add(stringRequest);
        initialize();
    }
    private void initialize() {

        double numDistance = distance;
        binding.tvTotaldistance.setText(Double.toString(distance));
        switch (vehicle_type)
        {
            case 1: //bike
                calculateBikeFare(numDistance);
                break;
            case 2: //car
                calculateCarFare(numDistance);
                break;
        }

    }
    private void calculateCarFare(double dist) {
        double fare;
        if(dist<=6)
        {
            fare = 100.0;
            displayDetails(fare);
            return;
        }
        fare = pricePerKm*dist;
        displayDetails(fare);
    }
    private void calculateBikeFare(double dist) {
        double fare;
        if(dist<=8)
        {
            fare = 50.0;
            displayDetails(fare);
            return;
        }
        fare = pricePerKm*dist;
        displayDetails(fare);
    }
    private void displayDetails(double fare) {
        binding.tvTotalfare.setText(""+fare);
        binding.tvYourfee.setText(""+fare/2);
        binding.tvTotalReceiveable.setText(""+fare/2);
    }
    private void clickListeners() {

        binding.bReceiveCash.setOnClickListener(view->{
            confirmPayment();
        });
    }

    private void confirmPayment(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Billing.this);

        builder1.setTitle("Alert!");
        builder1.setMessage("Are You Sure You Received Money?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    finish();
                    startActivity(new Intent(Billing.this, RideComplete.class));
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        builder1.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(Billing.this, RideComplete.class));
    }

}