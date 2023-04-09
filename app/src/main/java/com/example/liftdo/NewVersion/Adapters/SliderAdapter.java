package com.example.liftdo.NewVersion.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class SliderAdapter extends FragmentStatePagerAdapter {

    FragmentManager manager;
    List<Fragment> list;
    public SliderAdapter(@NonNull FragmentManager manager, List<Fragment> list) {
        super(manager);
        this.list = list;
        this.manager = manager;
    }
    @Override
    public Fragment getItem(int pos) {
        return list.get(pos);
    }
    @Override
    public  int getCount() {
        return list.size();
    }
}
