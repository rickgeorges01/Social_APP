package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagramclone.Fragments.HomeFragment;
import com.example.instagramclone.Fragments.NotificationFragment;
import com.example.instagramclone.Fragments.ProfileFragment;
import com.example.instagramclone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Initialisation à null pour les cas où aucun fragment n'est sélectionné
                Fragment selectorFragment = null;
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    selectorFragment = new HomeFragment();
                } else if (id == R.id.nav_search) {
                    selectorFragment = new SearchFragment();
                } else if (id == R.id.nav_add) {
                    selectorFragment = null;
                    startActivity(new Intent(HomeActivity.this, PostActivity.class));
                } else if (id == R.id.nav_heart) {
                    selectorFragment = new NotificationFragment();
                } else if (id == R.id.nav_profile) {
                    selectorFragment = new ProfileFragment();
                }

                // Vérifie si un fragment a été sélectionné
                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }
                // Indique que l'événement de sélection a été traité
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }
}