package com.example.fridgee;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;

public class AddItemActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    ActivityResultLauncher<Uri> pictureLauncher;
    Uri imageUri;
    private ImageView addCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        imageUri = createUri();
        registerPictureLauncher();

        addCamera = findViewById(R.id.upload_user_profile);
        addCamera.setOnClickListener( v -> {

        });


    }

    private Uri createUri() {
        File imageFile = new File(getApplicationContext().getFilesDir(), "fridgee_photo.jpg.");
        return FileProvider.getUriForFile(
                getApplicationContext(),
                "com.example.fridgee.camera_permission.fileProvider",
                imageFile
        );
    }

    private void registerPictureLauncher() {
        pictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
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