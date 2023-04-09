package com.example.liftdo.AllActivities.Startup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.databinding.ActivityVerifyOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTP extends AppCompatActivity {

    ActivityVerifyOtpBinding binding;
    String id ,phNo;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTexts();
        initUI();
        clickListeners();
    }

    private void clickListeners() {
        binding.bVerifyOTP.setOnClickListener(v->{
            getTexts();
        });

    }
    private void getTexts() {
        String phoneOTP = binding.inputCode1.getText().toString() + binding.inputCode2.getText().toString()
                + binding.inputCode3.getText().toString()+ binding.inputCode4.getText().toString()
                + binding.inputCode5.getText().toString()+ binding.inputCode6.getText().toString();

        if(phoneOTP.length()<6) {
            Toast.makeText(this, "Invalid Code!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(id !=null) {
            binding.bVerifyOTP.setVisibility(View.GONE);
            binding.progressCircularV.setVisibility(View.VISIBLE);
            verifycode(phoneOTP);
        }
    }
    private void startSignUp() {
        finish();
        Intent intent = new Intent(this, Signup.class);
        intent.putExtra("phonenumber", phNo);
        startActivity(intent);
    }

    private void setTexts() {
        mAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        phNo = "0"+extras.getString("phonenumber");
        binding.textMobile.setText("+92-"+extras.getString("phonenumber"));
        id = extras.getString("id");
    }
    private void initUI() {
        binding.inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    binding.inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void verifycode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, Code);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(VerifyOTP.this, "Phone Verified", Toast.LENGTH_SHORT).show();
                            binding.bVerifyOTP.setVisibility(View.GONE);
                            binding.progressCircularV.setVisibility(View.VISIBLE);
                            startSignUp();
                        }
                        else {
                            binding.bVerifyOTP.setVisibility(View.VISIBLE);
                            binding.progressCircularV.setVisibility(View.GONE);
                            Toast.makeText(VerifyOTP.this, "Enter a valid code!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}