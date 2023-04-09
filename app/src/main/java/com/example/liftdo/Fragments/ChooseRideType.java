package com.example.liftdo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.liftdo.AllActivities.Requests.AvailableRiders;
import com.example.liftdo.AllActivities.WaitScreens.BikeWait;
import com.example.liftdo.AllActivities.WaitScreens.CarWait;
import com.example.liftdo.Model.PassengerRequest;
import com.example.liftdo.Model.PutRequest;
import com.example.liftdo.Model.Users;
import com.example.liftdo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ChooseRideType extends BottomSheetDialogFragment {

    RadioGroup group;
    Button confirm;
    RadioButton rb;
    ProgressBar progressBar;
    Context context;
    View view;
    String ID;
    String distance, time;
    PassengerRequest requestJSON;

    public ChooseRideType() {
        // Required empty public constructor
    }
    public ChooseRideType(Context context,
                          String distance,
                          String time,
                          PassengerRequest requestJSON,
                          String ID) {
        this.time = time;
        this.distance = distance;
        this.context = context;
        this.requestJSON = requestJSON;
        this.ID = ID;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_choose_ride_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        getViews();
    }

    private void getViews() {
        confirm = view.findViewById(R.id.b_requestRide);
        progressBar = view.findViewById(R.id.progress_circularChooseRide);
        group = view.findViewById(R.id.radioGroup);
        TextView dist = view.findViewById(R.id.tv_distance);
        TextView tv_time = view.findViewById(R.id.tv_estTime);
        dist.setText("Distance: " + distance);
        tv_time.setText("Estimated Time: " + time);
        clickListeners();
    }

    private void clickListeners() {
        confirm.setOnClickListener(view -> {
            confirm.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            try{
                getUserData();
            }
            catch (Exception e){
                e.printStackTrace();
                confirm.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getUserData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(ID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    ref.removeEventListener(this);
                    selectRideType(users);
                } else {
                    Toast.makeText(context, "No such user found", Toast.LENGTH_SHORT).show();
                    confirm.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                confirm.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void selectRideType(Users users) {

        int check = group.getCheckedRadioButtonId();
        if (check == -1) {
            confirm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Select Ride Type!", Toast.LENGTH_SHORT).show();
            return;
        }
        rb = view.findViewById(check);
        String vehicle = rb.getText().toString();
        if (vehicle.equals("Car")) {
            String type = "Car";
            confirm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(context, CarWait.class);
            intent.putExtra("emailR", users.getEmail());
            intent.putExtra("ID", users.getID());
            sendRequest(users, intent, type);
        } else if (vehicle.equals("Bike")) {
            String type = "Bike";
            confirm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(context, BikeWait.class);
            intent.putExtra("emailR", users.getEmail());
            intent.putExtra("ID", users.getID());
            sendRequest(users, intent, type);
        }
    }
    private void sendRequest(@NonNull Users users, Intent intent, String type) {
        try {

            this.dismiss();
            requestJSON.setvType(type);
            AvailableRiders availableRiders = new AvailableRiders(ID, requestJSON);
            availableRiders.show(getActivity().getSupportFragmentManager(), availableRiders.getTag());

        } catch (Exception e) {
            e.printStackTrace();
            confirm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void userlater(){
//        PutRequest request = new PutRequest();
//        request.setUID(users.getID());
//        request.setvType(type);
//        request.setPhno(users.getPhNo());
//        request.setName(users.getUsername());
//        request.setDropPointLong(requestJSON.getDouble("dropPointLongitude"));
//        request.setDropPointLat(requestJSON.getDouble("dropPointLatitude"));
//        request.setPickPointLong(requestJSON.getDouble("pickPointLongitude"));
//        request.setPickPointLat(requestJSON.getDouble("pickPointLatitude"));
//        request.setAddressLineP(requestJSON.getString("addressLineP"));
//        request.setAddressLineD(requestJSON.getString("addressLineD"));
//        FirebaseDatabase.getInstance().getReference().child("PutRequests").child(ID).setValue(request);
//        Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
    }




}