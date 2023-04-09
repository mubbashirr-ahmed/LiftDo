package com.example.liftdo.AllActivities.MapActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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
import com.example.liftdo.databinding.ActivityPassengerRideTrackBinding;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class PassengerRideTrack extends AppCompatActivity implements
        OnMapReadyCallback,
        OnConnectionFailedListener, TaskLoadedCallback {

    private GoogleMap mMap;
    private ActivityPassengerRideTrackBinding binding;
    private AcceptedRequests acceptedRequests;
    private SupportMapFragment mapFragment;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPassengerRideTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPassengerTrack);
        getRiderData();
        mapFragment.getMapAsync(this);
        clickListeners();
    }

    private void getRiderData() {
        Gson gson = new Gson();
        acceptedRequests = gson.fromJson(getIntent().getStringExtra("AcceptedRequests"), AcceptedRequests.class);
        binding.tvDriverName.setText(acceptedRequests.getDriverName());
        binding.tvVehicleNumber.setText(acceptedRequests.getVehicleNum());
    }

    private void clickListeners() {
        binding.btnPassengerCancel.setOnClickListener(v -> cancelRequest());
    }

    private void cancelRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRMATION");  // Set the title of the dialog
        builder.setMessage("Are you sure you want to cancel the ride?\n This may affect your reputation points."); // Set the message of the dialog
        builder.setCancelable(true);
        builder.setPositiveButton("OK", (dialog, which) -> {
            //Delete request from database!
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "Ready", Toast.LENGTH_SHORT).show();
        getMyLocation();
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

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {

            mMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(location.getLatitude(),
                                    location.getLongitude()),
                            17));
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false); //for accessing current location
            firebaseLocation();
        });
    }
    private void firebaseLocation() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Positions").child(acceptedRequests.getPassengerID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    RealTimePosition position = snapshot.getValue(RealTimePosition.class);
                    assert position != null;
                    if (position.isReach()) {
                        ref.removeEventListener(this);
                        ref.removeValue();
                        finish();
                        Intent intent = new Intent(PassengerRideTrack.this, Billing.class);
                        intent.putExtra("distance", 5);
                        // intent.putExtra("riderId", riderID);
                        startActivity(intent);
                    }
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(position.getLatitude(), position.getLongitude())));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PassengerRideTrack.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });


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



