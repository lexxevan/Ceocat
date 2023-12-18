package com.sauerkrauts.group.ceocat.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sauerkrauts.group.ceocat.R;
import com.sauerkrauts.group.ceocat.loginclasses.Login;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseUser user;
    FirebaseAuth auth;

    TextView textView;

    public AdminProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminProfile newInstance(String param1, String param2) {
        AdminProfile fragment = new AdminProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);


        TextView adminEmailAccount = view.findViewById(R.id.adminEmailAccount);
        ImageView logoutButton = view.findViewById(R.id.adminLogout);
        TextView adminName = view.findViewById(R.id.adminFullname);
        TextView adminDep = view.findViewById(R.id.adminDepartment);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's data in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/Admin/" + userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get user data from the snapshot
                        String userEmail = currentUser.getEmail();
                        String fullname = snapshot.child("fullname").getValue(String.class);
                        String department = snapshot.child("department").getValue(String.class);

                        // Set the text in the TextViews
                        adminEmailAccount.setText(userEmail);
                        adminName.setText(fullname);
                        adminDep.setText(department);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                    Log.e("AdminProfile", "Error reading user data", error.toException());
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login screen or any other screen
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();  // Optional: Close the current activity
            }
        });

        return view;
    }

}