package com.example.fridgee;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private EditText loginEmail, loginPassword;
    private Button loginBtn;
    private TextView loginSignup, loginForgotPass;
    private FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginSignup = findViewById(R.id.login_signup);
        loginForgotPass = findViewById(R.id.login_forgot_password);
        loginBtn = findViewById(R.id.login_button);
        loginEmail = findViewById(R.id.login_email_id);
        loginPassword = findViewById(R.id.login_password);
        authProfile = FirebaseAuth.getInstance();

        loginSignup.setOnClickListener(view -> {
            Intent signupActivity = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupActivity);
            Log.d(TAG, "onCreate: Started Signup Activity");
            finish();
            Log.d(TAG, "onCreate: Login Activity closed");
        });

        loginForgotPass.setOnClickListener(view -> {
            Intent forgotPassActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotPassActivity);
            Log.d(TAG, "onCreate: Started ForgotPassword Activity");
        });

        loginBtn.setOnClickListener(view -> {
            String email = loginEmail.getText().toString();
            String pass = loginPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
                loginEmail.setError("Email required");
                loginEmail.requestFocus();
                Log.i(TAG, "Empty email section, cannot proceed");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter email again", Toast.LENGTH_SHORT).show();
                loginEmail.setError("Valid email is required");
                loginEmail.requestFocus();
            } else if (TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                loginPassword.setError("Password required");
                loginPassword.requestFocus();
                Log.i(TAG, "Empty password section, cannot proceed");
            } else {
                loginUser(email, pass);
            }
        });
    }

    private void loginUser(String email, String pass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = dialog.findViewById(R.id.progress_layout_text);
        textView.setText("Loading...");
            authProfile.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                Intent openMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(openMain);
                finish();
                dialog.dismiss();
            } else {
                try {
                    throw task.getException();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    dialog.dismiss();
                }
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authProfile.getCurrentUser() != null) {
            Toast.makeText(this, "Already Logged in", Toast.LENGTH_SHORT).show();
            Intent openMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(openMain);
            finish();
        } else {
            Toast.makeText(this, "Login to use the app", Toast.LENGTH_SHORT).show();
        }
    }
}