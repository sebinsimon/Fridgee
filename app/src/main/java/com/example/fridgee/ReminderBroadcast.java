package com.example.fridgee;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    private static final String TAG = "ReminderBroadcast";
    @Override
    public void onReceive(Context context, Intent intent) {
        String itemId = intent.getStringExtra("Key");
        String itemName = intent.getStringExtra("itemName");
        String reminderDate = intent.getStringExtra("itemReminderDate");
        String expireDate = intent.getStringExtra("itemExpiryDate");


        if (reminderDate != null) {
            Log.d(TAG, "Notification for " + itemName);
            if (!isItemNotified(context, itemId, true)) {
                String notificationText = "You have a notification: " + itemName;
                showNotification(context, "Fridgee", notificationText);
                markItemAsNotified(context, itemId, true);
            }

        }
    }
    private void showNotification(Context context, String title, String text) {
        Intent openActivity = new Intent(context, ItemActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openActivity, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "fridgeeNotification")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.fridge_icon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(123, builder.build());
    }

    private boolean isItemNotified(Context context, String itemId, boolean isReminder) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = isReminder ? "reminder_" + itemId : "expire_" + itemId;
        return preferences.getBoolean(key, false);
    }

    private void markItemAsNotified(Context context, String itemId, boolean isReminder) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = isReminder ? "reminder_" + itemId : "expire_" + itemId;
        preferences.edit().putBoolean(key, true).apply();
    }
}
