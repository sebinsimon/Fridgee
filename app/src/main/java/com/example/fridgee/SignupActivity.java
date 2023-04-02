package com.example.fridgee;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {
    private TextView backToLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backToLogin = findViewById(R.id.back_to_login);
        backToLogin.setOnClickListener(view -> {
            Intent backToLogin = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(backToLogin);
            finish();
        });
    }
}