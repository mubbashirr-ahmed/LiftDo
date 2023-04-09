package com.example.liftdo.NewVersion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.liftdo.AllActivities.Dashboard.Accountsettings;
import com.example.liftdo.AllActivities.Dashboard.Home;
import com.example.liftdo.AllActivities.Dashboard.HowItWorks;
import com.example.liftdo.AllActivities.Dashboard.ScheduledRides;
import com.example.liftdo.AllActivities.Dashboard.Userforms.Contactus;
import com.example.liftdo.AllActivities.Dashboard.Userforms.Terms;
import com.example.liftdo.AllActivities.Startup.Login;
import com.example.liftdo.R;
import com.example.liftdo.databinding.ActivityLiftDoBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class LiftDoActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLiftDoBinding binding = ActivityLiftDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarLiftDo.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_scheduled,
                R.id.nav_help, R.id.nav_cu, R.id.nav_tc)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lift_do);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        NavigationUI.setupWithNavController(toolbar, navigationView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lift_do);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}