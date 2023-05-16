package com.example.fridgee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {
     TextView itemLocation, itemAddDate, itemExpireDate, itemWeight, itemReminderDate, itemNote;
     TextView itemName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemName = findViewById(R.id.add_item_name);
        itemLocation = findViewById(R.id.add_item_location);
        itemAddDate = findViewById(R.id.add_item_added_date);
        itemExpireDate = findViewById(R.id.add_item_expiry_date);
        itemWeight = findViewById(R.id.add_item_weight);
        itemReminderDate = findViewById(R.id.add_item_reminder);
        itemNote = findViewById(R.id.add_item_notes);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemName.setText(bundle.getString("itemName"));
            itemAddDate.setText("Added on: " + bundle.getString("itemAddedDate"));
            itemExpireDate.setText("Expire on: " + bundle.getString("itemExpiryDate"));
            itemWeight.setText("Weight: " + bundle.getString("itemWeight"));
            itemReminderDate.setText("Remind me on: " + bundle.getString("itemReminderDate"));
            itemNote.setText("Notes: " + bundle.getString("itemNotes"));
            itemLocation.setText("Location: " + bundle.getString("itemLocation"));

        }

    }
}