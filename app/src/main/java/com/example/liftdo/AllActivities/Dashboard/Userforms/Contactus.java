package com.example.liftdo.AllActivities.Dashboard.Userforms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityContactusBinding;
import com.example.liftdo.databinding.ActivityHowItWorksBinding;

public class Contactus extends AppCompatActivity {

    ActivityContactusBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar4.setNavigationOnClickListener(v -> finish());
    }
}