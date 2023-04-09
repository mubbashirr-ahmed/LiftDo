package com.example.liftdo.AllActivities.Cash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftdo.Model.CommissionModel;
import com.example.liftdo.R;
import com.example.liftdo.Recycler.CommissionAdapter;

import java.util.ArrayList;

public class CommissionDetails extends AppCompatActivity {

    ArrayList<CommissionModel> commissionModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_details);
        commissionModels = new ArrayList<>();
        commissionModels.add(new CommissionModel("250", "2/2/22"));
        commissionModels.add(new CommissionModel("350", "3/2/22"));
        commissionModels.add(new CommissionModel("450", "4/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        commissionModels.add(new CommissionModel("650", "5/2/22"));
        CommissionAdapter adapter = new CommissionAdapter(this, commissionModels);
        RecyclerView recyclerView = findViewById(R.id.rv_cdetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}