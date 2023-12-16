package com.sauerkrauts.group.ceocat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sauerkrauts.group.ceocat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public enum NavigationItem {
        HOMEPAGE(R.id.homepageadmin),
        INPUT(R.id.inputAdmin),
        PROFILE(R.id.profileAdmin);

        private final int itemId;

        NavigationItem(int itemId) {
            this.itemId = itemId;
        }

        public int getItemId() {
            return itemId;
        }

        public static NavigationItem fromItemId(int itemId) {
            for (NavigationItem item : values()) {
                if (item.getItemId() == itemId) {
                    return item;
                }
            }
            // Return a default item or handle the case accordingly
            return HOMEPAGE;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AdminHome());



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            NavigationItem navigationItem = NavigationItem.fromItemId(itemId);

            switch (navigationItem) {
                case HOMEPAGE:
                    replaceFragment(new AdminHome());
                    break;
                case INPUT:
                    replaceFragment(new AdminInput());
                    break;
                case PROFILE:
                    replaceFragment(new AdminProfile());
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}