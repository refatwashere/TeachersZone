package com.refat.teacherszone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.refat.teacherszone.utils.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroupTextSize;
    private RadioButton radioSmall, radioMedium, radioLarge;
    private Button buttonApplySettings, buttonLogoutFromSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI components
        radioGroupTextSize = findViewById(R.id.radioGroupTextSize);
        radioSmall = findViewById(R.id.radioSmall);
        radioMedium = findViewById(R.id.radioMedium);
        radioLarge = findViewById(R.id.radioLarge);
        buttonApplySettings = findViewById(R.id.buttonApplySettings);
        buttonLogoutFromSettings = findViewById(R.id.buttonLogoutFromSettings);

        // Load current settings and set radio button
        loadSettings();

        // Set up Apply Settings button click listener
        buttonApplySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySettings();
            }
        });

        // Set up Logout button click listener
        buttonLogoutFromSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void loadSettings() {
        String currentTextSize = SharedPreferencesManager.getTextSize(this);
        switch (currentTextSize) {
            case SharedPreferencesManager.TEXT_SIZE_SMALL:
                radioSmall.setChecked(true);
                break;
            case SharedPreferencesManager.TEXT_SIZE_MEDIUM:
                radioMedium.setChecked(true);
                break;
            case SharedPreferencesManager.TEXT_SIZE_LARGE:
                radioLarge.setChecked(true);
                break;
        }
    }

    private void applySettings() {
        String selectedTextSize;
        int checkedId = radioGroupTextSize.getCheckedRadioButtonId();

        if (checkedId == R.id.radioSmall) {
            selectedTextSize = SharedPreferencesManager.TEXT_SIZE_SMALL;
        } else if (checkedId == R.id.radioMedium) {
            selectedTextSize = SharedPreferencesManager.TEXT_SIZE_MEDIUM;
        } else if (checkedId == R.id.radioLarge) {
            selectedTextSize = SharedPreferencesManager.TEXT_SIZE_LARGE;
        } else {
            selectedTextSize = SharedPreferencesManager.TEXT_SIZE_MEDIUM; // Default
        }

        SharedPreferencesManager.setTextSize(this, selectedTextSize);
        Toast.makeText(this, "Settings applied! Restart app for full effect.", Toast.LENGTH_LONG).show();
        // In a more complex app, you might re-inflate layouts or use a theme change
        // For simplicity, we just tell the user to restart.
    }

    private void logoutUser() {
        SharedPreferencesManager.clearLoginData(this); // Clear login data
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back to LoginActivity and clear activity stack
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish SettingsActivity
    }
}