package com.example.fridgee;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final String TAG = "AddItemActivity";
    private ActivityResultLauncher<Uri> pictureLauncher;
    private String imageURL;
    private Uri imageUri;
    private ImageView addCamera;
    private EditText itemName, itemNotes;
    private Spinner itemLocation;
    private EditText addedDate, expireDate, reminderDate, itemWeight;
    private DatePickerDialog picker;
    private static final String[] listLocation = {"Fridge", "Freezer", "Pantry"};
    final Calendar calendar = Calendar.getInstance();
    private Button saveButton;

    private ActivityResultLauncher<String> galleryLauncher;

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

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            if (result.getResultCode() == CAMERA_PERMISSION_CODE) {
                                addCamera.setImageURI(imageUri);
                            } else if (result.getResultCode() == GALLERY_REQUEST_CODE) {
                                Uri imageUri = data.getData();
                                addCamera.setImageURI(imageUri);
                            }
                        }
                    }
                }
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_item, listLocation);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemLocation.setAdapter(adapter);
        addedDate.setOnClickListener(v -> {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(AddItemActivity.this, (view, year1, month1, dayOfMonth) -> addedDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
            // Set the minimum date of the calendar picker to the current date.
            picker.getDatePicker().setMaxDate(System.currentTimeMillis());
            picker.show();
        });

        reminderDate.setOnClickListener(v -> {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(AddItemActivity.this, (view, year12, month12, dayOfMonth) -> reminderDate.setText(dayOfMonth + "/" + (month12 + 1) + "/" + year12), year, month, day);


            long reminderDateMillis = calendar.getTimeInMillis();
            picker.getDatePicker().setMinDate(reminderDateMillis);
            picker.getDatePicker().setMinDate(System.currentTimeMillis());
            picker.show();
        });

        expireDate.setOnClickListener(v -> {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(AddItemActivity.this, (view, year13, month13, dayOfMonth) -> expireDate.setText(dayOfMonth + "/" + (month13 + 1) + "/" + year13), year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis());
            picker.show();
        });


        addCamera.setOnClickListener( v -> cameraPermission());

        saveButton.setOnClickListener( v -> saveData());

    }
    public void saveData() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            String userId = firebaseUser.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().
                    child("Item Images").child(userId).child(UUID.randomUUID().toString());

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddItemActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }).addOnFailureListener(e -> dialog.dismiss());
        }
    }

    private void uploadData() {

        String name = itemName.getText().toString();
//        Get the selected option from the spinner
        String location = itemLocation.getSelectedItem().toString();
        String notes = itemNotes.getText().toString();
        String addDate = addedDate.getText().toString();
        String expiryDate = expireDate.getText().toString();
        String reminder = reminderDate.getText().toString();
        String weight = itemWeight.getText().toString();


//            Get current user UID
            String userId = firebaseUser.getUid();
//            Get child node under Registered User
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);


            ListData listData = new ListData(name, addDate, expiryDate, reminder, location, notes, weight, imageURL);

//            Create alert progress bar
            AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();
//           Write the item data to the database.
            referenceProfile.child("items").push().setValue(listData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Item successfully added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to added, try again later", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                        dialog.dismiss();
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
                        Log.e(TAG, exception.getMessage());
                    }
                }
        );
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        if (result != null) {
                            addCamera.setImageURI(null);
                            addCamera.setImageURI(result);
                            imageUri = result;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        Log.e(TAG, exception.getMessage());
                    }
                }
        );
    }

    private void cameraPermission() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                if (ActivityCompat.checkSelfPermission(AddItemActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddItemActivity.this, new String[]
                            {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                }
                else {
                    pictureLauncher.launch(imageUri);
                }
            } else if (options[item].equals("Choose from Gallery")) {
                galleryLauncher.launch("image/*");
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
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
                Log.d(TAG, "User denied camera permission");
            }
        }
    }
}