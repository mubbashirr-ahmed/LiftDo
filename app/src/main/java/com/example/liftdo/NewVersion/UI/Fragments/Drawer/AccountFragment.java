package com.example.liftdo.NewVersion.UI.Fragments.Drawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.liftdo.AllActivities.Startup.Login;
import com.example.liftdo.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.bLogout.setOnClickListener(v->{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.apply();
            getActivity().finish();
            startActivity(new Intent(getContext(), Login.class));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}