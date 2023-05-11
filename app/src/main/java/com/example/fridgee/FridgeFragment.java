package com.example.fridgee;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.example.fridgee.databinding.ActivityMainBinding;
import com.example.fridgee.databinding.FragmentFridgeBinding;

import java.util.ArrayList;

public class FridgeFragment extends Fragment {

    ActivityMainBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentFridgeBinding binding = FragmentFridgeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        int[] image = {R.drawable.baseline_person_24, R.drawable.baseline_add_a_photo_24};
        String[] name = {"Grapes", "Apple"};
        String[] addDate = {"12/05/2022", "15/05/2022"};
        String[] expireDate = {"15/05/2022", "19/05/2022"};
        String[] reminder = {"13/05/2022", "18/05/2022"};
        String[] weight = {"110gm", "250gm"};
        String[] notes = {"Check before eat", "Wash before eat"};

        ArrayList<ListData> listData1 = new ArrayList<>();

        for (int i = 0; i < image.length; i++){
            ListData listData2 = new ListData( name[i], addDate[i], expireDate[i], reminder[i], weight[i], notes[i], image[i]);
            listData1.add(listData2);
        }

// May need to change the code or add implementation
//        ListAdapter listAdapter1 = new ListAdapter(getContext(), listData1);
//        binding.listViewFridge.setAdapter(listAdapter1);
        binding.listViewFridge.setClickable(true);
        binding.listViewFridge.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent itemActivity = new Intent(getActivity(), ItemActivity.class);
                itemActivity.putExtra("Grapes", name);
                itemActivity.putExtra("Added:", addDate);
                itemActivity.putExtra("Expire:", expireDate);
                itemActivity.putExtra("Reminder:", reminder);
                itemActivity.putExtra("Weight:", weight);
                itemActivity.putExtra("Notes", notes);
            }
        });
        return view;
    }
}