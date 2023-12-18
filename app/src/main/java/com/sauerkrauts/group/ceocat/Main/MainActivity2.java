package com.sauerkrauts.group.ceocat.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.sauerkrauts.group.ceocat.Associate.AssociateConfirm;
import com.sauerkrauts.group.ceocat.Associate.AssociateProfile;
import com.sauerkrauts.group.ceocat.Associate.AssociateUpdate;
import com.sauerkrauts.group.ceocat.R;
import com.sauerkrauts.group.ceocat.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;

    public enum NavigationItem {
        UPDATE(R.id.updateAssc),
        CONFIRM(R.id.confirmAssc),
        PROFILE(R.id.profileAssc);

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
            return UPDATE;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AssociateUpdate());



        binding.bottomNavigationView2.setOnItemSelectedListener( item -> {
            int itemId = item.getItemId();
            NavigationItem navigationItem = NavigationItem.fromItemId(itemId);

            switch (navigationItem) {
                case UPDATE:
                    replaceFragment(new AssociateUpdate());
                    break;
                case CONFIRM:
                    replaceFragment(new AssociateConfirm());
                    break;
                case PROFILE:
                    replaceFragment(new AssociateProfile());
                    break;
            }

            return true;
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, fragment);
        fragmentTransaction.commit();
    }
}