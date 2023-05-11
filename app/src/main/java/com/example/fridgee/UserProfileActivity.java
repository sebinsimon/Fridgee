package com.example.fridgee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UserProfileActivity extends AppCompatActivity {

    private Button editButton;
    private EditText userFullName, userEmail, userPass;
    private ViewGroup myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        editButton = findViewById(R.id.edit_button);
        userFullName = findViewById(R.id.user_full_name);
        userEmail = findViewById(R.id.user_email_id);
        userPass = findViewById(R.id.user_password);

        // By Default all EditText is disabled
        userFullName.setEnabled(false);
        userEmail.setEnabled(false);
        userPass.setEnabled(false);

        editButton.setOnClickListener(view1 -> {
            if (userFullName.isEnabled()){
//                Save the instances
                String user_name = userFullName.getText().toString();
                String user_email = userEmail.getText().toString();
                String user_pass = userPass.getText().toString();

//                Disable the EditText
                userFullName.setEnabled(false);
                userEmail.setEnabled(false);
                userPass.setEnabled(false);
//                Change Button text to "Edit" when EditText is disabled
                editButton.setText("Edit");
            }
            else {
                userFullName.setEnabled(true);
                userEmail.setEnabled(true);
                userPass.setEnabled(true);

                userFullName.requestFocus();
//                Change Button text to "Save" when EditText is enabled
                editButton.setText("Save");
            }
        });
    }
}