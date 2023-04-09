package com.example.liftdo.AllActivities.Dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftdo.AllActivities.Startup.Login;
import com.example.liftdo.Model.Passenger;
import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityAccountsettingsBinding;
import com.example.liftdo.databinding.ActivityHomeBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Accountsettings extends AppCompatActivity {

    ActivityAccountsettingsBinding binding;
    SharedPreferences preferences;
    Passenger passenger;
    String passwordUrl = "http://192.168.173.253:8081/passenger/update-password/";
    String phoneNumberUrl = "http://192.168.173.253:8081/passenger/update-phone-number/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountsettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        preferences = getSharedPreferences("LiftDoPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String ok = preferences.getString("gson", "null");
        passenger = gson.fromJson(ok, Passenger.class);
        clickListeners();
    }

    private void clickListeners() {
        binding.toolbarSettings.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        binding.btnChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //First verify with otp
                changePhoneNumber();
            }
        });
    }

    private void changePassword() {
        String currentPass = binding.etCurrentPass.getText().toString();
        String newPassword = binding.etNewPassword.getText().toString();
        String confirmNewPassword = binding.etConfirmPassword.getText().toString();

        if(currentPass== null || newPassword == null || confirmNewPassword == null){
            Toast.makeText(Accountsettings.this, "Fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (matchCurrentPassword(currentPass)) {
            if (newPassword.equals(confirmNewPassword)) {
                changingPassword(newPassword);
            } else {
                Toast.makeText(Accountsettings.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Accountsettings.this, "Wrong current password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean matchCurrentPassword(String currentPassword) {
        return currentPassword.equals(passenger.getPassword());
    }

    private void changingPassword(String newPassword) {
        String newUrl = passwordUrl + passenger.getEmail() + "/" + newPassword;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl, response -> {

            try {
                JSONObject obj = new JSONObject(response);
                int check = obj.getInt("id");
                if (check != 0) {
                    Toast.makeText(Accountsettings.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    //Update the preferences here
                    binding.etCurrentPass.setText("");
                    binding.etNewPassword.setText("");
                    binding.etConfirmPassword.setText("");
                }
            } catch (JSONException ex) {
                Log.e("The error is: ", ex.toString());
                ex.printStackTrace();
            }
        }, error -> {
            Toast.makeText(Accountsettings.this, "No internet connection", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });
        queue.add(stringRequest);
    }

    private void changePhoneNumber(){
        String newPhoneNumber = binding.etChangeNum.getText().toString();
        if(newPhoneNumber.equals(null)){
            Toast.makeText(Accountsettings.this, "Enter the new phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = phoneNumberUrl + Long.parseLong(newPhoneNumber);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject obj = new JSONObject(response);
                int check = obj.getInt("id");
                if (check != 0) {
                    Toast.makeText(Accountsettings.this, "Phone number changed successfully", Toast.LENGTH_SHORT).show();
                    //Update the preferences here
                    binding.etChangeNum.setText("");
                }
            } catch (JSONException ex) {
                Log.e("The error is: ", ex.toString());
                ex.printStackTrace();
            }
        }, error -> {
            Toast.makeText(Accountsettings.this, "No internet connection", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });
        queue.add(stringRequest);
    }
}
