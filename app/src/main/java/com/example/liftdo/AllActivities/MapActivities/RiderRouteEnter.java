package com.example.liftdo.AllActivities.MapActivities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftdo.Fragments.RequestsFragment;
import com.example.liftdo.Model.FetchURL;
import com.example.liftdo.Model.PutRequest;
import com.example.liftdo.Model.TaskLoadedCallback;
import com.example.liftdo.Model.Users;
import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityRiderRouteEnterBinding;
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
import com.google.firebase.database.FirebaseDatabase;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RiderRouteEnter extends AppCompatActivity implements OnMapReadyCallback,
        OnConnectionFailedListener,
        TaskLoadedCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ActivityRiderRouteEnterBinding binding;
    private LatLng myPos, destination;
    private Polyline currentPolyline;

    String destinationAddress;
    Thread dt;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "" + connectionResult, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initComponents();
        getLocationPermission();
        new Thread(this::autoCompleteMethod).start();
        clickListeners();
    }

    private void initComponents() {
        binding = ActivityRiderRouteEnterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRider);

    }
    private void getLocationPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mapFragment.getMapAsync(RiderRouteEnter.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        //Show SnackBar and prompt to settings
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }
    private void autoCompleteMethod() {
        Places.initialize(getApplicationContext(), getString(R.string.maps_API_KEY));
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autoCompltFragRider);
        assert autocompleteSupportFragment != null;

        autocompleteSupportFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(33.6909, 73.0169),  //SW
                new LatLng(33.6909, 73.0169))); //Northeast corner
        autocompleteSupportFragment.setCountries("PK");

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG);
        autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteSupportFragment.setPlaceFields(placeFields);

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destination = place.getLatLng();
                dt = new Thread(() -> getDistanceTime());
                dt.start();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Objects.requireNonNull(place.getLatLng()), 17));
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                String url = createURL(myPos, place.getLatLng());
                new FetchURL(RiderRouteEnter.this).execute((url), "driving");
                calculateDistance(url);
                binding.bClearMapR.setVisibility(View.VISIBLE);
            }
        });

    }
    private void getDistanceTime() {
        Geocoder geocoder = new Geocoder(RiderRouteEnter.this, Locale.getDefault());
        try {
            List<Address> list2 = geocoder.getFromLocation(destination.latitude, destination.longitude, 1);
            destinationAddress = list2.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void clickListeners() {
        Users users = getUser();

        binding.ivGpsRider.setOnClickListener(view -> getMyLocation());
        binding.bConfirmRouteRider.setOnClickListener(view -> {

            try {

                dt.join();
                PutRequest putRequest = new PutRequest();
                putRequest.setUID(users.getID());
                putRequest.setName(users.getUsername());
                putRequest.setvType(users.getVehicleType());
                putRequest.setvName(users.getVehicleNum());
                putRequest.setPhno(users.getPhNo());
                putRequest.setPickPointLat(myPos.latitude);
                putRequest.setPickPointLong(myPos.longitude);
                putRequest.setDropPointLat(destination.latitude);
                putRequest.setDropPointLong(destination.longitude);
                putRequest.setAddressLineD(destinationAddress);

                FirebaseDatabase.getInstance().getReference().child("AvailableRiders")
                        .child(users.getID()).setValue(putRequest);

                RequestsFragment rf = new RequestsFragment(users, myPos, destinationAddress, getApplicationContext());
                rf.show(getSupportFragmentManager(), rf.getTag());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(this, "Ex" + e, Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }


        });
        binding.bClearMapR.setOnClickListener(v -> {
            mMap.clear();
            binding.bConfirmRouteRider.setVisibility(View.GONE);
            getMyLocation();
            binding.bClearMapR.setVisibility(View.GONE);
        });
    }
    private Users getUser() {
        Gson gson = new Gson();
        String riderDataObjectAsAString = getIntent().getStringExtra("RiderInfo");
        return gson.fromJson(riderDataObjectAsAString, Users.class);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        getMyLocation();
        Toast.makeText(this, "Ready!", Toast.LENGTH_SHORT).show();
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
        binding.bConfirmRouteRider.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray arr = jsonObject.getJSONArray("routes");
                String distance = arr.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
                Toast.makeText(this, distance, Toast.LENGTH_SHORT).show();
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