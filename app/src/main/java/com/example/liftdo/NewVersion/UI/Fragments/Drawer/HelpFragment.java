package com.example.liftdo.NewVersion.UI.Fragments.Drawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liftdo.NewVersion.Adapters.FAQAdapter;
import com.example.liftdo.R;
import com.example.liftdo.databinding.FragmentHelpBinding;


public class HelpFragment extends Fragment {

    FragmentHelpBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init();
    }

    private void Init() {
        FAQAdapter adapter = new FAQAdapter(getContext(), null);
        binding.rvFaq.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFaq.setAdapter(adapter);
    }
}