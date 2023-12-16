package com.sauerkrauts.group.ceocat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sauerkrauts.group.ceocat.appversion2.Login;

public class AssociateProfile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AssociateProfile() {
        // Required empty public constructor
    }

    public static AssociateProfile newInstance(String param1, String param2) {
        AssociateProfile fragment = new AssociateProfile();
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
        View view = inflater.inflate(R.layout.fragment_associate_profile, container, false);

        TextView asscEmailAccount = view.findViewById(R.id.asscEmailAccount);
        ImageView logoutButton = view.findViewById(R.id.asscLogout);
        TextView asscFullname = view.findViewById(R.id.asscFullname);
        TextView asscWarehouseNumber = view.findViewById(R.id.asscWarehouseNumber);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/Associate/" + userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userEmail = currentUser.getEmail();
                        String fullname = snapshot.child("fullname").getValue(String.class);
                        String warehouseNumber = snapshot.child("warehouse").getValue(String.class);

                        asscEmailAccount.setText(userEmail);
                        asscFullname.setText(fullname);
                        asscWarehouseNumber.setText(warehouseNumber);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("AssociateProfile", "Error reading user data", error.toException());
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
