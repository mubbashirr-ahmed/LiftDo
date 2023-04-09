package com.example.liftdo.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftdo.Model.PassengerRequest;
import com.example.liftdo.Model.Users;
import com.example.liftdo.R;
import com.example.liftdo.Recycler.RequestAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private ProgressBar pb;
    private ArrayList<PassengerRequest> requestArrayList;
    private View rootView;
    private final Users users;
    private final LatLng myPos;
    private final String destination;
    RequestAdapter adapter;

    Context ctx;

    public RequestsFragment(Users users, LatLng myPos, String destination, Context ctx) {
        this.users = users;
        this.myPos = myPos;
        this.ctx = ctx;
        this.destination = destination;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_requests, container, false);
        init();
        return rootView;
    }
    private void init() {
        initComponents();
    }
    private void initComponents() {
        requestArrayList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.rv_requests);
        pb = rootView.findViewById(R.id.progress_requestWait);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setRequestsAdapter();
        fetchRequests();
    }
    private void fetchRequests() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("RequestedFromPassengers")
                .child(users.getID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        PassengerRequest passengerRequest = data.getValue(PassengerRequest.class);
                        assert passengerRequest != null;
                        if (distancebtwpoints(passengerRequest.getpLat(), passengerRequest.getpLong()) &&
                                !users.getID().equals(passengerRequest.getID())) {
                            requestArrayList.add(passengerRequest);
                            adapter.notifyDataSetChanged();
                            pb.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pb.setVisibility(View.GONE);
                Toast.makeText(ctx, "Unable to process your request! Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean distancebtwpoints(double PpickPointLat, double PpickPointLong) {
        float[] results = new float[1];
        Location.distanceBetween(
                myPos.latitude,
                myPos.longitude,
                PpickPointLat,
                PpickPointLong, results);
        double val = results[0] / 1000;
        Log.i("disssss1", String.valueOf(val));

        return val <= 3;
        //also set distance to view
    }


    private void setRequestsAdapter() {
        adapter = new RequestAdapter(getActivity(), requestArrayList, users, myPos);
        recyclerView.setAdapter(adapter);
    }
}