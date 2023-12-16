package com.sauerkrauts.group.ceocat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sauerkrauts.group.ceocat.referenceclasses.User;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private DatabaseReference databaseReference;

    public LiveData<User> getCurrentUser() {
        if (currentUser.getValue() == null) {
            currentUser.setValue(new User());
        }
        return currentUser;
    }

    public String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getUid();
        }
        return null;
    }

    public void fetchCurrentUser() {
        String userId = getCurrentUserId();
        if (userId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Associate").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        currentUser.setValue(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                    if (databaseError != null) {
                        String errorMessage = databaseError.getMessage();
                        // Log or display the error message as needed
                        Log.e("UserViewModel", "Database Error: " + errorMessage);
                    }
                }
            });
        }
    }
}
