package com.example.liftdo.NewVersion.UI.Fragments.DashBoard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liftdo.NewVersion.Model.AcceptedRequests;
import com.example.liftdo.R;
import com.example.liftdo.databinding.FragmentOnGoingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnGoingFragment extends Fragment {

    private FragmentOnGoingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnGoingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getInfo();
    }

    private void getInfo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String ID = mAuth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AcceptedRequests").child(ID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AcceptedRequests acceptedRequests = snapshot.getValue(AcceptedRequests.class);
                    binding.progress.setVisibility(View.GONE);
                    binding.cardAvailable.setVisibility(View.VISIBLE);
                    setViews(acceptedRequests);
                    reference.removeEventListener(this);
                }
                else{
                    binding.progress.setVisibility(View.GONE);
                    binding.tvNoRides.setVisibility(View.VISIBLE);
                    reference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progress.setVisibility(View.GONE);
                binding.tvNoRides.setVisibility(View.VISIBLE);
                reference.removeEventListener(this);
            }
        });
    }

    private void setViews(AcceptedRequests acceptedRequests) {
        binding.tvName.setText("Driver Name: " + acceptedRequests.getDriverName());
        binding.tvDestination.setText("Destination: "+acceptedRequests.getDropPoint());
        binding.tvVehicleNumber.setText("Vehicle Number: " + acceptedRequests.getVehicleNum());
        binding.tvPhoneNumber.setText("Phone Number: " +acceptedRequests.getDriverPhno());

    }
}