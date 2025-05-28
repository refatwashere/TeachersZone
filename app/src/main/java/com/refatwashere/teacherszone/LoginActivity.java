package com.refatwashere.teacherszone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.refatwashere.teacherszone.database.AppDatabase;
import com.refatwashere.teacherszone.database.User;
import com.refatwashere.teacherszone.utils.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword, editSchool;
    private Button buttonLogin, buttonSignUp;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferencesManager.initializeFirstRun(this);

        if (SharedPreferencesManager.isLoggedIn(this)) {
            navigateToMainActivity();
            return;
        }

        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        editSchool = findViewById(R.id.editTextSchool);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        db = AppDatabase.getInstance(this);

        buttonLogin.setOnClickListener(v -> loginUser());
        buttonSignUp.setOnClickListener(v -> signUpUser());
    }

    private void loginUser() {
        String email = safeGet(editEmail);
        String password = safeGet(editPassword);

        if (!isValidInput(email, password)) return;

        new Thread(() -> {
            User user = db.userDao().getUserByEmailAndPassword(email, password);
            runOnUiThread(() -> {
                if (user != null) {
                    SharedPreferencesManager.setLoggedIn(this, true);
                    SharedPreferencesManager.setUserId(this, user.getId());
                    SharedPreferencesManager.setUserEmail(this, user.getEmail());
                    SharedPreferencesManager.setSchoolName(this, user.getSchoolName());
                    navigateToMainActivity();
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void signUpUser() {
        String email = safeGet(editEmail);
        String password = safeGet(editPassword);
        String school = safeGet(editSchool);

        if (!isValidInput(email, password) || school.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User existing = db.userDao().getUserByEmail(email);
            runOnUiThread(() -> {
                if (existing != null) {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    User newUser = new User(email, password, school);
                    new Thread(() -> {
                        long id = db.userDao().insertUser(newUser);
                        runOnUiThread(() -> {
                            if (id > 0) {
                                SharedPreferencesManager.setLoggedIn(this, true);
                                SharedPreferencesManager.setUserId(this, (int) id);
                                SharedPreferencesManager.setUserEmail(this, newUser.getEmail());
                                SharedPreferencesManager.setSchoolName(this, newUser.getSchoolName());
                                navigateToMainActivity();
                            } else {
                                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            });
        }).start();
    }

    private boolean isValidInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String safeGet(TextInputEditText edit) {
        return edit.getText() != null ? edit.getText().toString().trim() : "";
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
