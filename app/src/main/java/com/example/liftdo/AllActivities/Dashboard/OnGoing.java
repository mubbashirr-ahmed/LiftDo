package com.example.liftdo.AllActivities.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityOnGoingBinding;
import com.google.firebase.database.FirebaseDatabase;

public class OnGoing extends AppCompatActivity {

    ActivityOnGoingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnGoingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkifany();
    }

    private void checkifany() {

    }
}