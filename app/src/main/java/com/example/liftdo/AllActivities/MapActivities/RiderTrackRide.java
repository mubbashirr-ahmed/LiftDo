package com.example.liftdo.AllActivities.MapActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.liftdo.AllActivities.Cash.Billing;
import com.example.liftdo.Model.AcceptedRequests;
import com.example.liftdo.Model.RealTimePosition;
import com.example.liftdo.Model.TaskLoadedCallback;
import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityRiderTrackRideBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class RiderTrackRide extends AppCompatActivity implements OnMapReadyCallback,
        OnConnectionFailedListener, TaskLoadedCallback {

    private GoogleMap mMap;
    private ActivityRiderTrackRideBinding binding;
    SupportMapFragment mapFragment;
    LatLng myPos, pasPickUpLocation;
    Polyline currentPolyline;
    String riderID, passengerID;
    String riderName, vehicleNum;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initComponents();
        clickListeners();
        trackRider();
        //createPollyLine();
    }

    private void initComponents() {
        binding = ActivityRiderTrackRideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTrackRider);
        passengerID = getIntent().getStringExtra("passengerID");
        //getFragmentManager().popBackStack();
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "Ready", Toast.LENGTH_SHORT).show();
        getMyLocation();
        getUserData();
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                myPos = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 17));
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });
    }
    private void getUserData() {
        Gson gson = new Gson();
        Log.i("eeeeee", "Here is 1");
        AcceptedRequests acceptedRequests = gson.fromJson(getIntent().getStringExtra("acceptedRequest"), AcceptedRequests.class);
        Log.i("eeeeee", "Here is 2");
        riderID = acceptedRequests.getDriverID();
        Log.i("eeID", riderID);
        riderName = acceptedRequests.getDriverName();
        Log.i("eeName", riderName);
        vehicleNum = acceptedRequests.getVehicleNum();
        Log.i("eeVehicle", vehicleNum);
        pasPickUpLocation = new LatLng(acceptedRequests.getLat(), acceptedRequests.getLon());
        Log.i("eeLoc", pasPickUpLocation.toString());
        createPollyLine();

    }
    private void createPollyLine() {
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pasPickUpLocation, 17f));
        mMap.addMarker(new MarkerOptions().position(pasPickUpLocation));
        String  url = createURL(myPos, pasPickUpLocation);

    }
    private void trackRider() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updatePos());
            }
        }, 3000, 5000);
    }
    private void updatePos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location ->  {
            RealTimePosition users = new RealTimePosition();
            users.setLatitude(location.getLatitude());
            users.setLongitude(location.getLongitude());
            users.setReach(false);
            FirebaseDatabase.getInstance().getReference().child("Positions").child(passengerID).setValue(users);
            Toast.makeText(this, "Pushed", Toast.LENGTH_SHORT).show();
        });
    }
    private void clickListeners() {
        binding.bRiderCancel.setOnClickListener(v -> {
            timer.cancel();
            FirebaseDatabase.getInstance().getReference().child("Positions").child(passengerID).removeValue();
            //FirebaseDatabase.getInstance().getReference().child("AcceptedRequests").child(passengerID).removeValue();
            Toast.makeText(RiderTrackRide.this, "Ride Cancelled!", Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.bRideStart.setOnClickListener(view -> {
            if (binding.bRideStart.getText().toString().equals("Reached")) {
                confirmRideDialogue("Arrival confirmation", "Have you arrived?", "Reached");
            } else {
                confirmRideDialogue("Ride ending confirmation", "Do you want to end the ride?", String.valueOf(R.string.endride));
            }
        });
    }
    private void confirmRideDialogue(String title, String message, String buttonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RiderTrackRide.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    if (buttonText.equals("Reached")) {
                        //send notification FCM.send
                        timer.cancel();
                        binding.bRideStart.setText(R.string.endride);
                        binding.bRiderCancel.setVisibility(View.GONE);
                    } else {
                        //database.getReference().child("Positions").child(passengerEmail.replace(".", "")).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Positions").child(passengerID).child("reach").setValue(true);
                        startActivity(new Intent(this, Billing.class));
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private String createURL(LatLng currPos, LatLng dest) {
        String str_origin = "origin=" + currPos.latitude + "," + currPos.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        return "https://maps.googleapis.com/maps/api/directions/json" + "?" + str_origin + "&" + str_dest + "&" + "mode=" + "driving" + "&key=" + getString(R.string.maps_API_KEY);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "" + connectionResult, Toast.LENGTH_SHORT).show();

    }
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapFragment.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapFragment.onSaveInstanceState(outState);
    }
}