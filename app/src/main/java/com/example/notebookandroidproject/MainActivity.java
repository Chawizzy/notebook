package com.example.notebookandroidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.notebookandroidproject.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    /*
     Variables
     */
    private Fragment selectorFragment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Initializing the variables
        */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        /*
        Allows me to move between the home fragment and profile fragment.
        */
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menuHome) {
                selectorFragment = new HomeFragment();
            } else if (id == R.id.menuProfile) {
                selectorFragment = new ProfileFragment();
            }

            if (selectorFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, selectorFragment)
                        .commit();
            }

            return true;
        });

        /*
        Opens the home fragment automatically after CreateAccountActivity or LoginActivity".
        */
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new HomeFragment())
                .commit();
    }


    @Override
    public void onBackPressed() {
        /*
        Allows user to exit after clicking the back button twice within two seconds
        */
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click Back Again To Exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}