package com.example.fridgee;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.Manifest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final String TAG = "AddItemActivity";

    ActivityResultLauncher<Uri> pictureLauncher;
    Uri imageUri;
    private ImageView addCamera;
    private EditText itemName, itemNotes;
    private Spinner itemLocation;
    private EditText addedDate, expireDate, reminderDate, itemWeight;
    private DatePickerDialog picker;
    private static final String[] listLocation = {"Fridge", "Freezer", "Pantry"};
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        imageUri = createUri();
        registerPictureLauncher();

        addCamera = findViewById(R.id.upload_user_profile);
        saveButton = findViewById(R.id.upload_save_button);
        itemName = findViewById(R.id.upload_item_name);
        itemLocation = findViewById(R.id.upload_location);
        itemNotes = findViewById(R.id.upload_notes);
        addedDate = findViewById(R.id.upload_added_date);
        expireDate = findViewById(R.id.upload_expiry_date);
        reminderDate = findViewById(R.id.upload_reminder);
        itemWeight = findViewById(R.id.upload_weight);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_item, listLocation);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemLocation.setAdapter(adapter);
        addedDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(AddItemActivity.this, (view, year1, month1, dayOfMonth) -> addedDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        reminderDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(AddItemActivity.this, (view, year12, month12, dayOfMonth) -> reminderDate.setText(dayOfMonth + "/" + (month12 + 1) + "/" + year12), year, month, day);
            picker.show();
        });

        expireDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(AddItemActivity.this, (view, year13, month13, dayOfMonth) -> expireDate.setText(dayOfMonth + "/" + (month13 + 1) + "/" + year13), year, month, day);
            picker.show();
        });



        addCamera.setOnClickListener( v -> {
            cameraPermission();
        });

        saveButton.setOnClickListener( v -> {
            uploadData();
        });

    }

    private void uploadData() {

        String name = itemName.getText().toString();
//        Get teh selected option from the spinner
        String location = itemLocation.getSelectedItem().toString();
        String notes = itemNotes.getText().toString();
        String addDate = addedDate.getText().toString();
        String expiryDate = expireDate.getText().toString();
        String reminder = reminderDate.getText().toString();
        String weight = itemWeight.getText().toString();


//        Get current logged in user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Create a map of the item data.
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemName", name);
        itemData.put("itemLocation", location);
        itemData.put("itemNotes", notes);
        itemData.put("itemAddedDate", addDate);
        itemData.put("itemExpiryDate", expiryDate);
        itemData.put("itemReminderDate", reminder);
        itemData.put("itemWeight", weight);


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
//       Write the item data to the database.
        referenceProfile.child("items").push().setValue(itemData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Item successfully added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                try {
                    throw task.getException();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to added, try again later", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private Uri createUri() {
        File imageFile = new File(getApplicationContext().getFilesDir(), "fridgee_photo.JPEG.");
        return FileProvider.getUriForFile(
                getApplicationContext(),
                "com.example.fridgee.camera_permission.fileProvider",
                imageFile
        );
    }

    private void registerPictureLauncher() {
        pictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    try {
                        if (result) {
                            addCamera.setImageURI(null);
                            addCamera.setImageURI(imageUri);
                        }
                    }
                    catch (Exception exception) {
                        exception.getStackTrace();
                    }
                }
        );
    }

    private void cameraPermission() {
        if (ActivityCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddItemActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else {
            pictureLauncher.launch(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pictureLauncher.launch(imageUri);
            }
            else {
                Toast.makeText(this, "Camera permission denied, permission required to take picture", Toast.LENGTH_SHORT).show();
            }
        }
    }
}