package com.example.fridgee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FloatingActionButton fabHome;
    private FirebaseAuth authProfile;
    private FirebaseUser currentUser;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fabHome = findViewById(R.id.fab_home);

        viewPager2.setAdapter(new FragmentAdapter(this));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position){
                        case 0:
                            // Can add icons here by using parenthesis {tab.seticon}
                            tab.setText("Fridge");
//                                tab.setIcon(getResources().getDrawable(R.drawable.baseline_person_24));
                            break;
                        case 1:
                            tab.setText("Freezer");
                            break;
                        case 2:
                            tab.setText("Pantry");
                            break;
                    }
                });
        tabLayoutMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
//        App crash on this line of code in night mode
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:{
                    Toast.makeText(MainActivity.this, "Home button clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.nav_user:{
                    Intent userAc = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(userAc);
                    Toast.makeText(MainActivity.this, "User account button clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.nav_settings:{
                    Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingsActivity);
                    Toast.makeText(MainActivity.this, "Settings button clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.nav_logout:{
                    userLogout();
                    Toast.makeText(MainActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }
            }

            return false;
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddItem = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(openAddItem);
            }
        });
    }

    private void userLogout() {
        authProfile = FirebaseAuth.getInstance();
        currentUser = authProfile.getCurrentUser();
        authProfile.signOut();
        Intent openLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(openLogin);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}