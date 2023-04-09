package com.example.liftdo.AllActivities.Requests;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftdo.Model.PassengerRequest;
import com.example.liftdo.Model.PutRequest;
import com.example.liftdo.R;
import com.example.liftdo.Recycler.ChooseRiderAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableRiders extends BottomSheetDialogFragment {
    String PID;
    PassengerRequest object;
    ArrayList<PutRequest> list;
    RecyclerView recyclerView;
    ChooseRiderAdapter adapter;

    public AvailableRiders(String PID, PassengerRequest object) {
        this.PID = PID;
        this.object = object;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.available_riders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_available);
        getRiders();
    }

    private void getRiders() {
        //String destination;
        //destination = object.getString("addressLineD");

        setAdapter(object.getpLat(), object.getpLong());
        getFirebaseData(object.getvType());


    }

    private void getFirebaseData(String vType) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("AvailableRiders").orderByChild("vType").equalTo(vType);
        Log.i("disssss2",vType);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        PutRequest putRequest = data.getValue(PutRequest.class);
                        Log.i("disssss3", putRequest.getUID());
                        Log.i("disssss4", PID);
                        if (distancebtwpoints(putRequest.getDropPointLat(), putRequest.getDropPointLong())
                                && (!putRequest.getUID().equals(PID))) {
                            list.add(putRequest);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean distancebtwpoints(double dropPointLat, double dropPointLong) {
        float[] results = new float[1];
        Location.distanceBetween(
                object.getdLat(),
                object.getdLong(),
                dropPointLat,
                dropPointLong, results);
        double val = results[0] / 1000;
        Log.i("disssss1", String.valueOf(val));

        return val <= 3;
        //also set distance to view
    }

    private void setAdapter(double pLat, double pLong) {
        list = new ArrayList<>();
        adapter = new ChooseRiderAdapter(getContext(), list, pLat, pLong, object);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
