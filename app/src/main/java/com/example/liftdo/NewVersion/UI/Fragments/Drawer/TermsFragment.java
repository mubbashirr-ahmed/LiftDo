package com.example.liftdo.NewVersion.UI.Fragments.Drawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liftdo.R;
import com.example.liftdo.databinding.FragmentTermsBinding;

public class TermsFragment extends Fragment {

    FragmentTermsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms, container, false);
    }
}