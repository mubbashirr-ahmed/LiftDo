package com.example.liftdo.NewVersion.UI.Fragments.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.liftdo.AllActivities.MapActivities.RiderRouteEnter;
import com.example.liftdo.AllActivities.Startup.RiderRegistration;
import com.example.liftdo.Model.Users;
import com.example.liftdo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LiftDoPager extends Fragment {

    TextView tv;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup vp = (ViewGroup) inflater.inflate(R.layout.pager_liftdo, container, false);

        CardView cv = vp.findViewById(R.id.cv_liftdoP);
        progressBar = vp.findViewById(R.id.progress_circularLiftDo);
        tv = vp.findViewById(R.id.tv_liftdo);

        cv.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            liftDo();
        });
        return vp;
    }

    public LiftDoPager() {
    }

    private void liftDo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String ID = mAuth.getCurrentUser().getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(ID);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    assert users != null;
                    String type = users.getUsertype();
                    if (type.equals("single")) {
                        progressBar.setVisibility(View.GONE);
                        tv.setVisibility(View.VISIBLE);
                        startActivity(new Intent(getContext(), RiderRegistration.class));
                    } else {
                        getDriverInfo(users);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDriverInfo(Users users) {
        Intent intent = new Intent(getContext(), RiderRouteEnter.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(users);
        intent.putExtra("RiderInfo", myJson);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
    }
}
