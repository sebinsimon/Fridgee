package com.example.fridgee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userFullName, userEmail, userPass;
    private ViewGroup myLayout;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userFullName = findViewById(R.id.user_full_name);
        userEmail = findViewById(R.id.user_email_id);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(currentUser.getUid());

        // Retrieve the full name data from the database and set it to the TextView
        userRef.child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.getValue(String.class);
                userFullName.setText(fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

//        String userName = currentUser.getDisplayName();
        String userEmailID = currentUser.getEmail();

//        userFullName.setText(userName);
        userEmail.setText(userEmailID);


    }
}