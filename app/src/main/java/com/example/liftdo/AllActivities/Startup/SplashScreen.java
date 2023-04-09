package com.example.liftdo.AllActivities.Startup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.AllActivities.Dashboard.Home;
import com.example.liftdo.AllActivities.WaitScreens.BikeWait;
import com.example.liftdo.MainActivity;
import com.example.liftdo.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences preferences = getSharedPreferences("LiftDoPrefs", MODE_PRIVATE);
        boolean loginKey = preferences.getBoolean("loginKey", false);

        //check warnings, status
        //check internet
        //check location permission
        //turn on location

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            finish();
            if (!loginKey) {
                startActivity(new Intent(SplashScreen.this, Login.class));
            } else
                startActivity(new Intent(SplashScreen.this, Home.class));
        }

    }
}
