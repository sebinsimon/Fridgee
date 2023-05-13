package com.example.fridgee;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    EditText signupFullName, signupEmail, signupPass, signupConfirmPass;
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

        signupFullName = findViewById(R.id.signup_full_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPass = findViewById(R.id.signup_password);
        signupConfirmPass = findViewById(R.id.signup_confirm_password);

        Button signupBtn = findViewById(R.id.signup_button);
        signupBtn.setOnClickListener(v -> {
            signupRegister();
        });

    }

    private void signupRegister() {
        String fullName = signupFullName.getText().toString();
        String email = signupEmail.getText().toString().trim();
        String pass = signupPass.getText().toString().trim();
        String confirmPass = signupConfirmPass.getText().toString().trim();


        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Full name is required to signup", Toast.LENGTH_SHORT).show();
            signupFullName.setError("Full name is required");
            signupFullName.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email required to signup", Toast.LENGTH_SHORT).show();
            signupEmail.setError("Email is required");
            signupEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            signupEmail.setError("Valid email is required");
            signupEmail.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Password is required to signup", Toast.LENGTH_SHORT).show();
            signupPass.setError("Password cannot be blank");
            signupPass.requestFocus();

        } else if (pass.length() < 6){
            Toast.makeText(this, "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
            signupPass.setError("Password is too short");
            signupPass.requestFocus();
        } else if (TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Confirm your password", Toast.LENGTH_SHORT).show();
            signupConfirmPass.setError("Confirm password");
            signupConfirmPass.requestFocus();
        } else if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Password mismatch, enter again", Toast.LENGTH_SHORT).show();
            signupConfirmPass.setError("Re-enter same password");
            signupConfirmPass.requestFocus();
            signupConfirmPass.clearComposingText();
        } else {
            registerUser(fullName, email, pass);
        }
    }

    private void registerUser(String fullName, String email, String pass) {

//        Check for internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (networkCapabilities == null || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.signup_button), "No internet connection, registration failed", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Turn On Wifi", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open the Wi-Fi settings
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            snackbar.show();
        } else {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignupActivity.this,
                    task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            ReadWriteUserDetails writeUserDetails = new  ReadWriteUserDetails(fullName);

//                            Get user reference from firebase
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
//                            add data using user's unique id
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        firebaseUser.sendEmailVerification();

                                        Intent openMain = new Intent(SignupActivity.this, MainActivity.class);
//                                       Prevent user returning to signup activity
                                        openMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(openMain);
                                        finish();
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Toast.makeText(SignupActivity.this, "Verification failed, try again later", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, e.getMessage());
                                        }
                                    }
                                }
                            });

                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
        }
    }
}