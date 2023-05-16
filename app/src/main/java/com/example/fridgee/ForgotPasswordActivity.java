package com.example.fridgee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";
    private Button forgotButton;
    private EditText forgotEmail;
    private FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmail = findViewById(R.id.forgot_email_id);
        forgotButton = findViewById(R.id.forgot_password_button);

        forgotButton.setOnClickListener(v -> {
            String email = forgotEmail.getText().toString();

            if (TextUtils.isEmpty(email)) {
                forgotEmail.setError("Email is required");
                forgotEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this, "Enter a valida Email for verification", Toast.LENGTH_SHORT).show();
                forgotEmail.setError("Valid Email required");
                forgotEmail.requestFocus();
            } else {
                resetUserPassword(email);
            }
        });

    }

    private void resetUserPassword(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(R.id.progress_layout_text);
        textView.setText("Sending reset password link...");

        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Check your inbox for link", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Successfully sent an email to user's email address");
                dialog.dismiss();
            } else {
                try {
                    throw task.getException();
                } catch (Exception e) {
                    Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, e.getMessage());
                    dialog.dismiss();
                }
            }
        });
    }
}