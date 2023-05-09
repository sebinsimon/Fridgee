package com.example.fridgee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 1:
                return new FreezerFragment();
            case 2:
                return new PantryFragment();
            case 0:
            default:
                return new FridgeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
