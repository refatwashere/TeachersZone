package com.refat.teacherszone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.refat.teacherszone.utils.SharedPreferencesManager; // Import our SharedPreferences helper
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewSchoolName;
    private MaterialCardView cardSyllabus, cardStudyMaterials, cardSettings;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewSchoolName = findViewById(R.id.textViewSchoolName);
        cardSyllabus = findViewById(R.id.cardSyllabus);
        cardStudyMaterials = findViewById(R.id.cardStudyMaterials);
        cardSettings = findViewById(R.id.cardSettings);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Display personalized welcome message and school name
        String userEmail = SharedPreferencesManager.getUserEmail(this);
        String schoolName = SharedPreferencesManager.getSchoolName(this);

        if (userEmail != null) {
            textViewWelcome.setText("Welcome, " + userEmail.split("@")[0] + "!"); // Show part of email
        } else {
            textViewWelcome.setText("Welcome, Teacher!");
        }

        if (schoolName != null && !schoolName.isEmpty()) {
            textViewSchoolName.setText("School: " + schoolName);
        } else {
            textViewSchoolName.setText("School: Not specified");
        }


        // Set up click listeners for the cards
        cardSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SyllabusManagerActivity.class);
                startActivity(intent);
            }
        });

        cardStudyMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudyMaterialsActivity.class);
                startActivity(intent);
            }
        });

        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Set up Logout Button click listener
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        SharedPreferencesManager.clearLoginData(this); // Clear all saved login data
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back to LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity stack
        startActivity(intent);
        finish(); // Finish MainActivity
    }
}