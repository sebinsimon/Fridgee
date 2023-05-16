package com.example.fridgee;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

     private Context context;
     private List<ListData> dataList;

    public RecyclerViewAdapter(Context context, List<ListData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recName.setText(dataList.get(position).getName());
        holder.recAddDate.setText("Added on: " + dataList.get(position).getAddDate());
        holder.recExpireDate.setText("Expire on: " +  dataList.get(position).getExpireDate());
        holder.recReminder.setText("Remind me on: " + dataList.get(position).getReminderDate());

        holder.recCard.setOnClickListener(v -> {
            Intent openItem = new Intent(context, ItemActivity.class);
            openItem.putExtra("itemName", dataList.get(holder.getAdapterPosition()).getName());
            openItem.putExtra("itemAddedDate", dataList.get(holder.getAdapterPosition()).getAddDate());
            openItem.putExtra("itemExpiryDate", dataList.get(holder.getAdapterPosition()).getExpireDate());
            openItem.putExtra("itemReminderDate", dataList.get(holder.getAdapterPosition()).getReminderDate());
            openItem.putExtra("itemLocation", dataList.get(holder.getAdapterPosition()).getLocation());
            openItem.putExtra("itemNotes", dataList.get(holder.getAdapterPosition()).getNotes());
            openItem.putExtra("itemWeight", dataList.get(holder.getAdapterPosition()).getWeight());
            context.startActivity(openItem);
        });
        }

//    }

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