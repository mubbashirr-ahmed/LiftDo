package com.example.liftdo.AllActivities.Dashboard.Userforms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityHowItWorksBinding;
import com.example.liftdo.databinding.ActivityTermsBinding;

public class Terms extends AppCompatActivity {

    ActivityTermsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar3.setNavigationOnClickListener(v -> finish());
    }
}