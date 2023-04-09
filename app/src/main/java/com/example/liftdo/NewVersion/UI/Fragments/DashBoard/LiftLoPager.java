package com.example.liftdo.NewVersion.UI.Fragments.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.liftdo.AllActivities.MapActivities.PassengerRouteEnter;
import com.example.liftdo.R;

public class LiftLoPager extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_lift_lo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv = view.findViewById(R.id.tvll);
        ProgressBar progressBar = view.findViewById(R.id.progress_circularLiftLo);
        view.findViewById(R.id.cardView10).setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            startActivity(new Intent(getContext(), PassengerRouteEnter.class));
            progressBar.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        });
    }

    public LiftLoPager() {
    }
}
