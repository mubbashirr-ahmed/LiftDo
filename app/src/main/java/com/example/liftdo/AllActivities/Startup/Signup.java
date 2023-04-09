package com.example.liftdo.AllActivities.Startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.Model.Users;
import com.example.liftdo.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Signup extends AppCompatActivity {

    ActivitySignupBinding signupBinding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String token, phno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signupBinding.getRoot());
        init();
    }
    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        phno = getIntent().getStringExtra("phonenumber");
        signupBinding.etPhoneNum.setText(phno);
        clickListeners();
    }
    private void clickListeners(){
        signupBinding.button.setOnClickListener(view -> {

            signupBinding.progressCircularSignUp.setVisibility(View.VISIBLE);
            signupBinding.button.setVisibility(View.GONE);
            registerPassenger();
        });
    }
    private void registerPassenger() {
        String email = signupBinding.etEmailAddress.getText().toString().trim();
        String username = signupBinding.etUsername.getText().toString();
        String cnic = signupBinding.etCnic.getText().toString();
        String password = signupBinding.etPassword.getText().toString();
        String conPassword = signupBinding.etConfirmPassword.getText().toString();

        if (email.isEmpty() || username.isEmpty() || cnic.isEmpty() || password.isEmpty() || conPassword.isEmpty()) {
            Toast.makeText(Signup.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            signupBinding.progressCircularSignUp.setVisibility(View.GONE);
            signupBinding.button.setVisibility(View.VISIBLE);
        } else {
            if (matchPasswords(password, conPassword)) {
                try {
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    signupBinding.progressCircularSignUp.setVisibility(View.GONE);
                                    signupBinding.button.setVisibility(View.VISIBLE);
                                    Toast.makeText(Signup.this, "Please try again later!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                token = task.getResult();
                                //add this token to preferences
                                //when logout clear preferences
                                //when login again call this function
                            });

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String id = task.getResult().getUser().getUid();
                                    Users users = new Users(token, email, username, "single", password, phno, cnic , id);
                                    users.setVehicleNum("None");
                                    users.setVehicleModel("None");
                                    users.setVehicleType("None");
                                    users.setLicenseNum("None");
                                    users.setStatus("clear");
                                    users.setWarnings(0);
                                    firebaseDatabase.getReference().child("Users").child(id).setValue(users);
                                    Toast.makeText(Signup.this, "User Created", Toast.LENGTH_SHORT).show();
                                    signupBinding.progressCircularSignUp.setVisibility(View.GONE);
                                    signupBinding.button.setVisibility(View.GONE);
                                    finish();
                                    startActivity(new Intent(Signup.this, Login.class));
                                } else {
                                    Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    signupBinding.progressCircularSignUp.setVisibility(View.GONE);
                                    signupBinding.button.setVisibility(View.VISIBLE);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    signupBinding.progressCircularSignUp.setVisibility(View.GONE);
                    signupBinding.button.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private boolean matchPasswords(String pass1, String pass2){
        if(!pass1.equals(pass2)){
            Toast.makeText(Signup.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            signupBinding.progressCircularSignUp.setVisibility(View.GONE);
            signupBinding.button.setVisibility(View.VISIBLE);
            return false;
        }
        else
            return true;
    }
}