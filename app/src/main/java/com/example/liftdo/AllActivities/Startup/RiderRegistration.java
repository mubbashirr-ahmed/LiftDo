package com.example.liftdo.AllActivities.Startup;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.databinding.ActivityRiderRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RiderRegistration extends AppCompatActivity {
    private String licenseNum;
    private String vehicleName;
    private String vehicleType;
    ActivityRiderRegistrationBinding riderRegistrationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        riderRegistrationBinding = ActivityRiderRegistrationBinding.inflate(getLayoutInflater());
        setContentView(riderRegistrationBinding.getRoot());

        init();
    }
    private void init() {
        clickListeners();
    }

    private void clickListeners() {
        riderRegistrationBinding.cbBike.setOnClickListener(view -> {
            riderRegistrationBinding.cbCar.setChecked(false);
            vehicleType = "Bike";
        });

        riderRegistrationBinding.cbCar.setOnClickListener(view -> {
            riderRegistrationBinding.cbBike.setChecked(false);
            vehicleType = "Car";
        });
        riderRegistrationBinding.submit.setOnClickListener(view -> checkFields());
        riderRegistrationBinding.toolbar10.setNavigationOnClickListener(v -> finish());
    }
    private void checkFields() {
        String vehicleNum = riderRegistrationBinding.etVehicleNum.getText().toString();
        licenseNum = riderRegistrationBinding.etLicenseNo.getText().toString();
        vehicleName = riderRegistrationBinding.etVehicleModel.getText().toString();

        if (vehicleNum.isEmpty() || vehicleName.isEmpty()) {
            Toast.makeText(RiderRegistration.this, "Please, fill all details", Toast.LENGTH_SHORT).show();
        } else if (!riderRegistrationBinding.cbBike.isChecked() &&
                !riderRegistrationBinding.cbCar.isChecked()) {
            Toast.makeText(RiderRegistration.this, "Please, select at least one option",
                    Toast.LENGTH_SHORT).show();
        } else {
            registerRider();
        }
    }
    private void registerRider() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        try{
            assert user != null;
            firebaseDatabase.getReference().child("Users").child(user.getUid()).child("licenseNum").setValue(licenseNum);
            firebaseDatabase.getReference().child("Users").child(user.getUid()).child("vehicleType").setValue(vehicleType);
            firebaseDatabase.getReference().child("Users").child(user.getUid()).child("vehicleNum").setValue(vehicleName);
            firebaseDatabase.getReference().child("Users").child(user.getUid()).child("usertype").setValue("Double");
            Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show();
            finish();
        }
        catch (Exception exception){
            Toast.makeText(this, ""+exception, Toast.LENGTH_SHORT).show();
            Log.e("Firemy" , exception.toString());
        }
    }
}



