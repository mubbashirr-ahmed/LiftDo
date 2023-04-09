package com.example.liftdo.AllActivities.WaitScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.AllActivities.MapActivities.PassengerRideTrack;
import com.example.liftdo.Model.AcceptedRequests;
import com.example.liftdo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class BikeWait extends AppCompatActivity {
    Button btn_wait;
    String email, ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_wait);
        init();
    }

    private void init() {
        initComponents();
        clickListener();
        firebaseLocation();
    }

    private void firebaseLocation() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AcceptedRequests").child(ID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(BikeWait.this, "Driver Found", Toast.LENGTH_SHORT).show();
                    ref.removeEventListener(this);
                    AcceptedRequests requests = snapshot.getValue(AcceptedRequests.class);
                    finish();
                    ref.removeValue();
                    Intent intent = new Intent(BikeWait.this, PassengerRideTrack.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(requests);
                    intent.putExtra("AcceptedRequests", myJson);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BikeWait.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initComponents() {
        btn_wait = findViewById(R.id.b_cancelWaitBike);
        email = getIntent().getStringExtra("emailR");
        ID = getIntent().getStringExtra("ID");
    }

    private void clickListener() {
        btn_wait.setOnClickListener(v -> alertDialog());
    }
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete entry");
        builder.setMessage("Are you sure you want to cancel?");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            FirebaseDatabase.getInstance().getReference().child("PutRequests").child(ID).removeValue();
            finish();
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }
}
