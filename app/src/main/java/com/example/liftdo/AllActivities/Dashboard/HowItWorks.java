package com.example.liftdo.AllActivities.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.liftdo.R;
import com.example.liftdo.Recycler.FAQAdapter;
import com.example.liftdo.databinding.ActivityHowItWorksBinding;

public class HowItWorks extends AppCompatActivity {

    ActivityHowItWorksBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHowItWorksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Init();
    }
    private void Init() {


        FAQAdapter adapter = new FAQAdapter(this, null);
        binding.rvFaq.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFaq.setAdapter(adapter);
    }

}