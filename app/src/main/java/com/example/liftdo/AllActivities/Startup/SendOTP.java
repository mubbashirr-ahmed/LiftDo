package com.example.liftdo.AllActivities.Startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.databinding.ActivitySendOtpBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {
    ActivitySendOtpBinding binding;
    FirebaseAuth mAuth;
    String verificationID;
    String phNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth= FirebaseAuth.getInstance();

        binding.buttonGetOTP.setOnClickListener(view -> {
            binding.buttonGetOTP.setVisibility(View.GONE);
            binding.progressCircular.setVisibility(View.VISIBLE);
            validate();
        });
    }

    private void validate() {
        phNo = binding.inputMobile.getText().toString().trim();

        if (phNo.length() < 10) {
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            binding.buttonGetOTP.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.GONE);
            return;
        }
        getOTP(phNo);
    }
    private void getOTP(String phno) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+92" + phno)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Toast.makeText(SendOTP.this, "User already exists", Toast.LENGTH_SHORT).show();
            binding.buttonGetOTP.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(SendOTP.this, "Verification Failed "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            binding.buttonGetOTP.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            binding.buttonGetOTP.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.GONE);
            Intent intent = new Intent(SendOTP.this, VerifyOTP.class);
            Bundle extras = new Bundle();
            extras.putString("phonenumber", phNo);
            extras.putString("id",verificationID);
            intent.putExtras(extras);
            startActivity(intent);
        }
    };
}