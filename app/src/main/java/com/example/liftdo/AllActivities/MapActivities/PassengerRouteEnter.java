package com.example.liftdo.AllActivities.MapActivities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftdo.Fragments.ChooseRideType;
import com.example.liftdo.Model.FetchURL;
import com.example.liftdo.Model.Passenger;
import com.example.liftdo.Model.PassengerRequest;
import com.example.liftdo.Model.TaskLoadedCallback;
import com.example.liftdo.Model.Users;
import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityPassengerRouteEnterBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PassengerRouteEnter extends AppCompatActivity implements OnMapReadyCallback,
        OnConnectionFailedListener,
        TaskLoadedCallback {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    Users users;
    LatLng myPos, destination;
    String distance, time;
    Polyline currentPolyline;
    ActivityPassengerRouteEnterBinding passengerRouteEnterBinding;
    PassengerRequest request;
    Thread t1;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        initComponents();
        getLocationPermission();
        new Thread(this::autoComplete).start();
        new Thread(this::getPassengerInfo).start();
        ClickListeners();
    }
    private void initComponents() {
        passengerRouteEnterBinding = ActivityPassengerRouteEnterBinding.inflate(getLayoutInflater());
        setContentView(passengerRouteEnterBinding.getRoot());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }
    private void getPassengerInfo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String ID = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(ID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    users = snapshot.getValue(Users.class);
                    reference.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        PassengerRouteEnter.this,
                        error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getLocationPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mapFragment.getMapAsync(PassengerRouteEnter.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }
    private void autoComplete() {
        Places.initialize(getApplicationContext(), getString(R.string.maps_API_KEY));
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autonewfrag);
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(33.6909, 73.0169),  //SW
                new LatLng(33.6909, 73.0169))); //NorthEast corner
        autocompleteSupportFragment.setCountries("PK");
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG);
        autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteSupportFragment.setPlaceFields(placeFields);

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {}
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destination = place.getLatLng();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Objects.requireNonNull(place.getLatLng()), 17));
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                String url = createURL(myPos, destination);
                new FetchURL(PassengerRouteEnter.this).execute((url), "driving");
                passengerRouteEnterBinding.bConfirmRoute.setVisibility(View.VISIBLE);
                t1 = new Thread(()->calculateDistance(url));
                t1.start();
                passengerRouteEnterBinding.bClearMapP.setVisibility(View.VISIBLE);
            }
        });
    }
    private void ClickListeners() {
        passengerRouteEnterBinding.ivGps.setOnClickListener(view -> getMyLocation());
        passengerRouteEnterBinding.bConfirmRoute.setOnClickListener(view -> showChooseRide());
        passengerRouteEnterBinding.bClearMapP.setOnClickListener(v -> {
            mMap.clear();
            passengerRouteEnterBinding.bConfirmRoute.setVisibility(View.GONE);
            getMyLocation();
            passengerRouteEnterBinding.bClearMapP.setVisibility(View.GONE);
        });
    }
    private void showChooseRide() {
        if(distance!=null){
            createJSON();
            ChooseRideType CRT = new ChooseRideType(this, distance, time, request, users.getID());
            CRT.show(getSupportFragmentManager(), CRT.getTag());
        }
        else {
            try {
                t1.join();
                ChooseRideType CRT = new ChooseRideType(this, distance, time, request, users.getID());
                CRT.show(getSupportFragmentManager(), CRT.getTag());
            } catch (InterruptedException e) {
                Toast.makeText(PassengerRouteEnter.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
        }
    }
    private void createJSON() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> list2 = geocoder.getFromLocation(destination.latitude, destination.longitude, 1);
            List<Address> list1 = geocoder.getFromLocation(myPos.latitude, myPos.longitude, 1);
            request = new PassengerRequest();
            request.setID(users.getID());
            request.setName(users.getUsername());
            request.setPhNo(users.getPhNo());
            request.setpLat(myPos.latitude);
            request.setpLong(myPos.longitude);
            request.setdLat(destination.latitude);
            request.setdLong(destination.longitude);
            request.setAddressLineD(list2.get(0).getAddressLine(0));
            request.setAddressLineP(list1.get(0).getAddressLine(0));
            request.setDistance(Double.parseDouble( distance.replace("km", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();
        Toast.makeText(this, "Ready", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
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
    private String createURL(LatLng currPos, LatLng dest) {
        String str_origin = "origin=" + currPos.latitude + "," + currPos.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        return "https://maps.googleapis.com/maps/api/directions/json" + "?" + str_origin + "&" + str_dest + "&" + "mode=" + "driving" + "&key=" + getString(R.string.maps_API_KEY);
    }
    private void calculateDistance(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray arr = jsonObject.getJSONArray("routes");
                distance = arr.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
                time = arr.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(request);
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
