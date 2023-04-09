package com.example.liftdo.NewVersion.UI.Fragments.DashBoard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.liftdo.R;
import com.example.liftdo.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    FragmentManager manager;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceFragment(new DashboardFragment());
        binding.navView2.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeNAV:
                    replaceFragment(new DashboardFragment());
                    break;
                case R.id.onGoingNAV:
                    replaceFragment(new OnGoingFragment());
                    break;
                case R.id.notificationNAV:
                    replaceFragment(new NotificationFragment());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}