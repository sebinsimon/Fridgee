package com.example.fridgee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private Button signupBtn, loginBtn;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupBtn = findViewById(R.id.signupBtn);
        forgotPass = findViewById(R.id.forgotPassLB);
        loginBtn = findViewById(R.id.loginBtn);

        signupBtn.setOnClickListener(view -> {
            Intent signupActivity = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupActivity);
            Log.d(TAG, "onCreate: Started Signup Activity");
            finish();
            Log.d(TAG, "onCreate: Login Activity closed");
        });

        forgotPass.setOnClickListener(view -> {
            Intent forgotPassActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotPassActivity);
            Log.d(TAG, "onCreate: Started ForgotPassword Activity");
        });

        loginBtn.setOnClickListener(view -> {
            Intent homeActivity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeActivity);
            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Successfully user logged in");
            finish();
        });

    }
}