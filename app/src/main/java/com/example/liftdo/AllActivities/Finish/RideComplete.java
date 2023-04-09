
package com.example.liftdo.AllActivities.Finish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.liftdo.AllActivities.Dashboard.Home;
import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityRideCompleteBinding;
import com.example.liftdo.databinding.ActivityRiderTrackRideBinding;

public class RideComplete extends AppCompatActivity {

    ActivityRideCompleteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRideCompleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.buttonEnd).setOnClickListener(view->
        {
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, Home.class));
    }
}