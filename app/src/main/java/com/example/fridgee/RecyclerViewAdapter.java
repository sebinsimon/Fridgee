package com.example.fridgee;



import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<ListData> dataList;
    private HashSet<Integer> notifiedItems;

    public RecyclerViewAdapter(Context context, List<ListData> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.notifiedItems = new HashSet<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FridgeeApp";
            String description = "Channel for fridgee";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("fridgeeNotification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImage()).into(holder.recImage);
        holder.recName.setText(dataList.get(position).getName());
        holder.recAddDate.setText("Added on: " + dataList.get(position).getAddDate());
        holder.recExpireDate.setText("Expire on: " + dataList.get(position).getExpireDate());
        holder.recReminder.setText("Remind me on: " + dataList.get(position).getReminderDate());

        String reminderDate = dataList.get(position).getReminderDate();
        String expireDate = dataList.get(position).getExpireDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());

        try {
            Date currentDate = sdf.parse(sdf.format(new Date()));
            if (reminderDate != null) {
                Date itemReminderDate = sdf.parse(reminderDate);
                if (itemReminderDate != null && itemReminderDate.equals(currentDate) && !notifiedItems.contains(position)) {
                    Intent reminderIntent = new Intent(context, ReminderBroadcast.class);
                    reminderIntent.putExtra("Key", dataList.get(position).getKey());
                    reminderIntent.putExtra("itemName", dataList.get(position).getName());
                    reminderIntent.putExtra("itemReminderDate", reminderDate);

                    PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(
                            context, position, reminderIntent, PendingIntent.FLAG_IMMUTABLE
                    );
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, itemReminderDate.getTime(), reminderPendingIntent);
                        notifiedItems.add(position);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Check if expireDate is null and item expire date is equal to today and item is not notified
        try {
            Date currentDate = sdf.parse(sdf.format(new Date()));
            if (expireDate != null) {
                Date itemExpireDate = sdf.parse(expireDate);
                Log.d("RecyclerView", "item expire date: " + itemExpireDate);
                Log.d("RecyclerView", "current date: " + currentDate);

                if (itemExpireDate.before(currentDate) && !notifiedItems.contains(position)) {
                    Intent expireIntent = new Intent(context, ReminderBroadcast.class);
                    expireIntent.putExtra("Key", dataList.get(position).getKey());
                    expireIntent.putExtra("itemName", dataList.get(position).getName());
                    expireIntent.putExtra("itemExpireDate", expireDate);

                    PendingIntent expirePendingIntent = PendingIntent.getBroadcast(
                            context, position, expireIntent, PendingIntent.FLAG_IMMUTABLE
                    );
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, itemExpireDate.getTime(), expirePendingIntent);
                        notifiedItems.add(position);
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

////        Compare the current date with reminder date
//        try {
//            Date currentDate = sdf.parse(sdf.format(new Date()));
//            Date itemReminderDate = sdf.parse(reminderDate);
//
//            if (itemReminderDate != null && itemReminderDate.equals(currentDate) && !notifiedItems.contains(position)) {
//                Log.d("RecyclerView", "itemReminder date: " + itemReminderDate);
//                Log.d("RecyclerView", "current date: " + currentDate);
//
//
//                Intent intent = new Intent(holder.itemView.getContext(), ReminderBroadcast.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(holder.itemView.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, itemReminderDate.getTime(), pendingIntent);
//
//                notifiedItems.add(position);
//
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Date currentDate = sdf.parse(sdf.format(new Date()));
//            Date itemExpireDate = sdf.parse(expireDate);
//
//            if (itemExpireDate != null && itemExpireDate.equals(currentDate) && !notifiedItems.contains(position)) {
//                Log.d("RecyclerView", "itemReminder date: " + itemExpireDate);
//                Log.d("RecyclerView", "current date: " + currentDate);
//
//
//                Intent intent = new Intent(holder.itemView.getContext(), ReminderBroadcast.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(holder.itemView.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, itemExpireDate.getTime(), pendingIntent);
//

//
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        holder.recCard.setOnClickListener(v -> {
            Intent openItem = new Intent(context, ItemActivity.class);
            openItem.putExtra("itemImage", dataList.get(holder.getAdapterPosition()).getImage());
            openItem.putExtra("itemName", dataList.get(holder.getAdapterPosition()).getName());
            openItem.putExtra("itemAddedDate", dataList.get(holder.getAdapterPosition()).getAddDate());
            openItem.putExtra("itemExpiryDate", dataList.get(holder.getAdapterPosition()).getExpireDate());
            openItem.putExtra("itemReminderDate", dataList.get(holder.getAdapterPosition()).getReminderDate());
            openItem.putExtra("itemLocation", dataList.get(holder.getAdapterPosition()).getLocation());
            openItem.putExtra("itemNotes", dataList.get(holder.getAdapterPosition()).getNotes());
            openItem.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
            openItem.putExtra("itemWeight", dataList.get(holder.getAdapterPosition()).getWeight());
            context.startActivity(openItem);
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recName, recAddDate, recExpireDate, recReminder;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recImage = itemView.findViewById(R.id.recycler_image);
            recName = itemView.findViewById(R.id.recycler_title);
            recAddDate = itemView.findViewById(R.id.recycler_added_date);
            recExpireDate = itemView.findViewById(R.id.recycler_expire_date);
            recReminder = itemView.findViewById(R.id.recycler_reminder_date);

            recCard = itemView.findViewById(R.id.recCard);


    }

}