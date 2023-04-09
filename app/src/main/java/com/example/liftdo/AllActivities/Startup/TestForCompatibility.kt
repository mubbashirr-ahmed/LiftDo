package com.example.liftdo.AllActivities.Startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.liftdo.R
import com.example.liftdo.databinding.ActivityTestForCompatibilityBinding
import com.google.android.gms.common.util.DataUtils

class TestForCompatibility : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_for_compatibility)
        //val ss = intent.getStringExtra("OK")
        //v//al tv = findViewById<TextView>(R.id.textView16)
        //tv.text = ss
    }
}