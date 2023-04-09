package com.example.liftdo.AllActivities.Startup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.liftdo.AllActivities.Dashboard.Home
import com.example.liftdo.NewVersion.LiftDoActivity
import com.example.liftdo.R
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    var key: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val pref = getSharedPreferences("LiftDoPrefs", MODE_PRIVATE)
        key = pref.getBoolean("loginKey", false)


        //check warnings, status
        //check internet
        //check location permission
        //turn on location

    }

    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance().currentUser
        if(auth!=null){
            finish()
            startActivity(Intent(this, LiftDoActivity::class.java))
        }
        else{
            finish()
            startActivity(Intent(this, Login::class.java))
        }
    }
}