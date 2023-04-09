package com.example.liftdo.NewVersion.UI.Fragments.DashBoard;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.liftdo.NewVersion.Adapters.SliderAdapter;
import com.example.liftdo.R;
import com.example.liftdo.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    FragmentDashboardBinding homeBinding;
    SharedPreferences preferences;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = getContext().getSharedPreferences("LiftDoPrefs", MODE_PRIVATE);
        homeBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }
    private void initView(){
        setHomeViews();
        setFragments();
        setSlider();
        clickListeners();
    }
    private void setHomeViews() {
        homeBinding.tvName.setText(preferences.getString("name", "xxxxx"));
    }
    private void setFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(new LiftDoPager());
        list.add(new LiftLoPager());
        PagerAdapter adapter = new SliderAdapter(getActivity().getSupportFragmentManager(), list);
        homeBinding.viewpager.setAdapter(adapter);
    }
    private void setSlider() {
        ArrayList<SlideModel> arrayList = new ArrayList<>();
        arrayList.add(new SlideModel(R.drawable.p1, null));
        arrayList.add(new SlideModel(R.drawable.p2, null));
        arrayList.add(new SlideModel(R.drawable.p3, null));
        arrayList.add(new SlideModel(R.drawable.p4, null));
        homeBinding.imageSlider.setImageList(arrayList, ScaleTypes.CENTER_CROP);
    }
    private void clickListeners() {
    }
}