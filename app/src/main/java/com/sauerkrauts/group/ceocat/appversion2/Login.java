package com.sauerkrauts.group.ceocat.appversion2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sauerkrauts.group.ceocat.ChooseUser;
import com.sauerkrauts.group.ceocat.MainActivity;
import com.sauerkrauts.group.ceocat.MainActivity2;
import com.sauerkrauts.group.ceocat.R;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        final EditText emailAddress = findViewById(R.id.emailAddress);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginButton);
        final TextView registerNowBtn = findViewById(R.id.registerNowButton);
        final Button backBtn1 = findViewById(R.id.backButtonLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailText = emailAddress.getText().toString();
                final String passwordText = password.getText().toString();

                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    // Validate user using Firebase Authentication
                    loginUser(emailText, passwordText);
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        backBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ChooseUser.class));
            }
        });

    }


    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Check the user's role and navigate accordingly
                            checkUserRole(email, password);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String email, String password) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Check under Admin node
        Query adminQuery = usersRef.child("Admin").orderByChild("email").equalTo(email.toLowerCase());
        adminQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot adminSnapshot) {
                if (adminSnapshot.exists()) {
                    // Admin user found
                    Log.d("UserTypeDebug", "Admin user found");
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish(); // Finish the LoginActivity to remove it from the back stack
                } else {
                    // User not found under Admin node, check under Associate node
                    Query associateQuery = usersRef.child("Associate").orderByChild("email").equalTo(email.toLowerCase());
                    associateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot associateSnapshot) {
                            if (associateSnapshot.exists()) {
                                // Associate user found
                                Log.d("UserTypeDebug", "Associate user found");
                                startActivity(new Intent(Login.this, MainActivity2.class));
                                finish(); // Finish the LoginActivity to remove it from the back stack
                            } else {
                                // User not found under Associate node or invalid credentials
                                Log.d("UserTypeDebug", "User not found or not an admin/associate");
                                Toast.makeText(Login.this, "Invalid credentials or not an admin/associate", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError associateError) {
                            // Handle the error for Associate node query
                            Log.e("DatabaseError", associateError.getMessage());
                            Toast.makeText(Login.this, "Database error: " + associateError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError adminError) {
                // Handle the error for Admin node query
                Log.e("DatabaseError", adminError.getMessage());
                Toast.makeText(Login.this, "Database error: " + adminError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
