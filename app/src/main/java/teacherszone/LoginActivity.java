package com.refat.teacherszone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.refat.teacherszone.database.AppDatabase; // We'll create this soon
import com.refat.teacherszone.database.User; // We'll create this soon
import com.refat.teacherszone.utils.SharedPreferencesManager; // We'll create this soon
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextSchool;
    private Button buttonLogin, buttonSignUp;
    private AppDatabase db; // Our Room database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is already logged in
        if (SharedPreferencesManager.isLoggedIn(this)) {
            navigateToMainActivity();
            return; // Stop onCreate to prevent showing login screen
        }

        // Initialize UI components
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextSchool = findViewById(R.id.editTextSchool);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Initialize Room Database
        db = AppDatabase.getInstance(this);

        // Set up Login Button click listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Set up Sign Up Button click listener
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login in a background thread as database operations should not be on main thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = db.userDao().getUserByEmailAndPassword(email, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            // User found, login successful
                            SharedPreferencesManager.setLoggedIn(LoginActivity.this, true);
                            SharedPreferencesManager.setUserId(LoginActivity.this, user.getId());
                            SharedPreferencesManager.setUserEmail(LoginActivity.this, user.getEmail());
                            SharedPreferencesManager.setSchoolName(LoginActivity.this, user.getSchoolName());

                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        } else {
                            // User not found or credentials incorrect
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void signUpUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String schoolName = editTextSchool.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(schoolName)) {
            Toast.makeText(this, "Please fill all fields for sign up", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform sign up in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Check if user already exists
                User existingUser = db.userDao().getUserByEmail(email);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (existingUser != null) {
                            Toast.makeText(LoginActivity.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Create new user
                            User newUser = new User(email, password, schoolName);
                            long userId = db.userDao().insertUser(newUser); // insertUser returns the new row ID

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (userId > 0) {
                                        SharedPreferencesManager.setLoggedIn(LoginActivity.this, true);
                                        SharedPreferencesManager.setUserId(LoginActivity.this, (int) userId); // Cast long to int if safe, or use long for ID
                                        SharedPreferencesManager.setUserEmail(LoginActivity.this, newUser.getEmail());
                                        SharedPreferencesManager.setSchoolName(LoginActivity.this, newUser.getSchoolName());

                                        Toast.makeText(LoginActivity.this, "Sign Up Successful! Welcome!", Toast.LENGTH_SHORT).show();
                                        navigateToMainActivity();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign Up Failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity so user can't go back to it with back button
    }
}