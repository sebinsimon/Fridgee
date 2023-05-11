package com.example.fridgee;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends ArrayAdapter<ListData> {
    public RecyclerViewAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList){
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        ListData listData = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView listImage = view.findViewById(R.id.recycler_fridge_image);
        TextView listName = view.findViewById(R.id.recycler_fridge_title);
        TextView listAddDate = view.findViewById(R.id.recycler_fridge_added_date);
        TextView listExpireDate = view.findViewById(R.id.recycler_fridge_expire_date);
        TextView listReminder = view.findViewById(R.id.recycler_fridge_reminder_date);

        listImage.setImageResource(listData.image);
        listName.setText(listData.name);
        listAddDate.setText(listData.addDate);
        listExpireDate.setText(listData.expireDate);
        listReminder.setText(listData.reminderDate);

        return view;
    }
}