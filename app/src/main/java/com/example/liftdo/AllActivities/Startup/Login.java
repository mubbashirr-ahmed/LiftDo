package com.example.liftdo.AllActivities.Startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.liftdo.AllActivities.Dashboard.Home;
import com.example.liftdo.Model.Users;
import com.example.liftdo.NewVersion.LiftDoActivity;
import com.example.liftdo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextView tv_register;
    EditText email, password;
    Button login;
    ProgressDialog dialog;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        initComponents();
        setClickListeners();
    }

    private void initComponents() {
        tv_register = findViewById(R.id.tv_register);
        login = findViewById(R.id.bLogin);
        email = findViewById(R.id.etLoginEmailAddress);
        password = findViewById(R.id.etLoginPassword);
        dialog = new ProgressDialog(Login.this);
        dialog.setMessage("Logging in");
        preferences = getSharedPreferences("LiftDoPrefs", MODE_PRIVATE);
    }

    private void setClickListeners() {
        login.setOnClickListener(view -> {
            dialog.show();
            authentication();
        });
        tv_register.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SendOTP.class);
            startActivity(intent);
        });
    }

    private void authentication() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        verifyUserInfo(user.getUid());
                    } else {
                        dialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyUserInfo(String UID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    String name = users.getUsername();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", name);
                    editor.apply();
                    ref.removeEventListener(this);
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LiftDoActivity.class));
                } else {
                    Toast.makeText(Login.this, "No such user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}