package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.capstoneproject.Database.StockDatabase;
import com.example.capstoneproject.Entity.News.News;
import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Service.APICall;
import com.example.capstoneproject.UI.Error.ErrorFragment;
import com.example.capstoneproject.UI.List.ListFragment;
import com.example.capstoneproject.UI.News.NewsFragment;
import com.example.capstoneproject.UI.Search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

// Main activity that controls the flow and display of the fragments to the user.
// This control is handled by a bottom navigation menu with 3 items. Any time one of
// the items is clicked, a switch statement is run to determine which is the correct
// fragment to display. By default, the ListFragment is displayed on app startup.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove action bar from view
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Run ListFragment by default
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ListFragment())
                .commit();

    }

    // Listener to for bottom navigation buttons
    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    // Switch on button id
                    switch (item.getItemId()){
                        case R.id.nav_my_list:
                            selectedFragment = new ListFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_news:
                            selectedFragment = new NewsFragment();
                            break;
                    }

                    // Display selected fragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();

                    return true;
                }
            };
}