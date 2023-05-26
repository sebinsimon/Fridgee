package com.example.fridgee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.util.UUID;

public class ItemActivity extends AppCompatActivity {
     private TextView itemLocation, itemAddDate, itemExpireDate, itemWeight, itemReminderDate, itemNote;
     private TextView itemName;
     private ImageView itemImage;
     private FirebaseUser firebaseUser;
     private FirebaseAuth firebaseAuth;
     private FloatingActionButton editButton, deleteButton;
     private String key = "";
     private String imageUrl = "";
     private static final String TAG = "ItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        LocalDate currentDate = LocalDate.now();

        itemName = findViewById(R.id.add_item_name);
        itemLocation = findViewById(R.id.add_item_location);
        itemAddDate = findViewById(R.id.add_item_added_date);
        itemExpireDate = findViewById(R.id.add_item_expiry_date);
        itemWeight = findViewById(R.id.add_item_weight);
        itemReminderDate = findViewById(R.id.add_item_reminder);
        itemNote = findViewById(R.id.add_item_notes);
        itemImage = findViewById(R.id.add_item_image);
        editButton = findViewById(R.id.edit_Button);
        deleteButton = findViewById(R.id.delete_Button);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemName.setText(bundle.getString("itemName"));
            itemAddDate.setText("Added on: " + bundle.getString("itemAddedDate"));
            itemExpireDate.setText("Expire on: " + bundle.getString("itemExpiryDate"));
            itemWeight.setText("Weight: " + bundle.getString("itemWeight"));
            itemReminderDate.setText("Remind me on: " + bundle.getString("itemReminderDate"));
            itemNote.setText("Notes: " + bundle.getString("itemNotes"));
            itemLocation.setText("Location: " + bundle.getString("itemLocation"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("itemImage");
            Glide.with(this).load(bundle.getString("itemImage")).into(itemImage);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            Log.d("ItemActivity", "item reminder: " + itemReminderDate);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("items");
                    FirebaseStorage storage = FirebaseStorage.getInstance();
//                    Check the reference and key are located in the same location as in firebase storage
                    Log.d(TAG, "Database reference: " + reference);
                    Log.d(TAG, "Key: " + key);

                    StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            reference.child(key).removeValue();
//                            Make sure the key is same as before removeValue is executed
                            Log.d(TAG, "Key: " + key);
                            Toast.makeText(ItemActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    });
                }
            });
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUpdate = new Intent(ItemActivity.this, UpdateActivity.class)
                        .putExtra("itemName", itemName.getText().toString())
                        .putExtra("itemLocation", itemLocation.getText().toString())
                        .putExtra("itemNote", itemNote.getText().toString())
                        .putExtra("itemAddDate", itemAddDate.getText().toString())
                        .putExtra("itemExpireDate", itemExpireDate.getText().toString())
                        .putExtra("itemReminderDate", itemReminderDate.getText().toString())
                        .putExtra("itemWeight", itemWeight.getText().toString());

                startActivity(openUpdate);

            }
        });

    }
}