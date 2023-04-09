package com.example.liftdo.AllActivities.Dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.liftdo.AllActivities.Cash.CommissionDetails;
import com.example.liftdo.AllActivities.Dashboard.Userforms.Contactus;
import com.example.liftdo.AllActivities.Dashboard.Userforms.Terms;
import com.example.liftdo.AllActivities.Startup.Login;
import com.example.liftdo.Fragments.LiftDoPager;
import com.example.liftdo.Fragments.LiftLoPager;
import com.example.liftdo.R;
import com.example.liftdo.Recycler.SliderAdapter;
import com.example.liftdo.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class Home extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActivityHomeBinding homeBinding;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        homeBinding.bottomNavHome.setSelectedItemId(R.id.homeNAV);
        preferences = getSharedPreferences("LiftDoPrefs", MODE_PRIVATE);
        initView();

    }
    private void initView(){
        setHomeViews();
        setFragments();
        setSlider();
        clickListeners();
    }

    private void setFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(new LiftDoPager());
        list.add(new LiftLoPager());
        PagerAdapter adapter = new SliderAdapter(getSupportFragmentManager(), list);
        homeBinding.viewpager.setAdapter(adapter);
    }
    private void setHomeViews() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menuwhite);
        setTitle("");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        homeBinding.tvName.setText(preferences.getString("name", "xxxxx"));
    }
    private void setSlider() {
        ArrayList<SlideModel> arrayList = new ArrayList<>();
        arrayList.add(new SlideModel(R.drawable.p1, null));
        arrayList.add(new SlideModel(R.drawable.p2, null));
        arrayList.add(new SlideModel(R.drawable.p3, null));
        arrayList.add(new SlideModel(R.drawable.p4, null));
        homeBinding.imageSlider.setImageList(arrayList, ScaleTypes.CENTER_CROP);
    }
    @SuppressLint("NonConstantResourceId")
    private void clickListeners(){
        homeBinding.bottomNavHome.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.onGoingNAV:
                    finish();
                    startActivity(new Intent(Home.this, OnGoing.class));
                    return true;
                case R.id.notificationNAV:
                    Toast.makeText(this, "Notifications Clicked", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_settings:
                Intent intent = new Intent(Home.this, Accountsettings.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                SharedPreferences.Editor editor =   preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.help:
                finish();
                startActivity(new Intent(Home.this, HowItWorks.class));
                return true;
            case R.id.nav_scheduled:
                Intent qw = new Intent(Home.this, ScheduledRides.class);
                startActivity(qw);
                break;
            case R.id.nav_tc:
                Intent i3 = new Intent(Home.this, Terms.class);
                startActivity(i3);
                break;
            case R.id.nav_cu:
                Intent i4 = new Intent(Home.this, Contactus.class);
                startActivity(i4);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}