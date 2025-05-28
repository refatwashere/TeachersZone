package com.refatwashere.teacherszone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.refatwashere.teacherszone.utils.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    private Button buttonStudyMaterials, buttonSyllabus, buttonSettings, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStudyMaterials = findViewById(R.id.buttonStudyMaterials);
        buttonSyllabus = findViewById(R.id.buttonSyllabus);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonStudyMaterials.setOnClickListener(v -> safeNavigate(StudyMaterialsActivity.class));
        buttonSyllabus.setOnClickListener(v -> safeNavigate(SyllabusActivity.class));
        buttonSettings.setOnClickListener(v -> safeNavigate(SettingsActivity.class));

        buttonLogout.setOnClickListener(v -> {
            SharedPreferencesManager.clearLoginData(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void safeNavigate(Class<?> target) {
        try {
            startActivity(new Intent(this, target));
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open screen", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Navigation error", e);
        }
    }
}
