package com.sauerkrauts.group.ceocat.appversion2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sauerkrauts.group.ceocat.ChooseUser;
import com.sauerkrauts.group.ceocat.R;

public class Register extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ceocat-910da-default-rtdb.asia-southeast1.firebasedatabase.app/");

        final EditText fullName = findViewById(R.id.fullName);
        final EditText email = findViewById(R.id.emailAddress);
        final EditText password = findViewById(R.id.password);
        final EditText confirmPassword = findViewById(R.id.confirmPassword);
        final Spinner userTypeSpinner = findViewById(R.id.userTypeSpinner);
        final Button registerBtn = findViewById(R.id.registerButton);
        final TextView loginNowBtn = findViewById(R.id.loginNowButton);
        final EditText departmentEditText = findViewById(R.id.departmentEditText);
        final EditText warehouseEditText = findViewById(R.id.warehouseEditText);
        final Button backbutton = findViewById(R.id.registerBackButton);

        // Set up an adapter for the spinner with two items: Admin and Associate
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        // Set a listener to handle the selected item in the Spinner
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedUserType = parentView.getItemAtPosition(position).toString();

                // Update UI based on the selected user type
                updateUIForUserType(selectedUserType, departmentEditText, warehouseEditText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, ChooseUser.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullNameTxt = fullName.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();
                final String userType = userTypeSpinner.getSelectedItem().toString();
                final String departmentTxt = departmentEditText.getText().toString();
                final String warehouseTxt = warehouseEditText.getText().toString();

                if (fullNameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Use Firebase Authentication to create a user
                    firebaseAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                            .addOnCompleteListener(Register.this, task -> {
                                if (task.isSuccessful()) {
                                    // User account creation successful
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                    if (firebaseUser != null) {
                                        // User type doesn't exist, proceed with registration in the Realtime Database
                                        DatabaseReference userRef = databaseReference.child("users").child(userType).child(firebaseUser.getUid());

                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                                                if (!snapshot.exists()) {
                                                    // User type doesn't exist, proceed with registration
                                                    userRef.child("fullname").setValue(fullNameTxt);
                                                    userRef.child("email").setValue(emailTxt);
                                                    userRef.child("userType").setValue(userType);

                                                    // Add additional data based on user type
                                                    if ("Admin".equals(userType)) {
                                                        userRef.child("department").setValue(departmentTxt);
                                                    } else if ("Associate".equals(userType)) {
                                                        userRef.child("warehouse").setValue(warehouseTxt);
                                                    }

                                                    Toast.makeText(Register.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                                                    // Navigate to the Login activity
                                                    startActivity(new Intent(Register.this, Login.class));

                                                    // Finish the Register activity to remove it from the back stack
                                                    finish();
                                                } else {
                                                    // User type already exists
                                                    Toast.makeText(Register.this, "User type is already registered", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle the error
                                            }
                                        });
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the login activity
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    private void updateUIForUserType(String userType, EditText departmentEditText, EditText warehouseEditText) {
        // Assuming that "Admin" and "Associate" are the user types
        if ("Admin".equals(userType)) {
            // Show Department EditText and hide Warehouse EditText
            departmentEditText.setVisibility(View.VISIBLE);
            warehouseEditText.setVisibility(View.GONE);
        } else if ("Associate".equals(userType)) {
            // Show Warehouse EditText and hide Department EditText
            warehouseEditText.setVisibility(View.VISIBLE);
            departmentEditText.setVisibility(View.GONE);
        } else {
            // Handle other user types if needed
            departmentEditText.setVisibility(View.GONE);
            warehouseEditText.setVisibility(View.GONE);
        }
    }
}
