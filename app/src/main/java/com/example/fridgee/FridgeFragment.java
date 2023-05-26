package com.example.fridgee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FridgeFragment extends Fragment {

     private RecyclerView recyclerView;
     private List<ListData> dataList;
     private DatabaseReference databaseReference;
     private ValueEventListener eventListener;
     private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);

        recyclerView = view.findViewById(R.id.list_view_fridge);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = dialog.findViewById(R.id.progress_layout_text);
        textView.setText("Loading...");


        dataList = new ArrayList<>();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), dataList);
        recyclerView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getUid();

        assert userId != null;
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("items");


        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    ListData listData = itemSnapshot.getValue(ListData.class);
                    listData.setKey(itemSnapshot.getKey());
                    if (listData.getLocation().equals("Fridge")) {
                        dataList.add(listData);
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        return view;
    }
}