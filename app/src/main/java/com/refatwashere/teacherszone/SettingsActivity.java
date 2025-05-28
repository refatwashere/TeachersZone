package com.refatwashere.teacherszone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.refatwashere.teacherszone.utils.DatabaseExporter;
import com.refatwashere.teacherszone.utils.SharedPreferencesManager;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private Button buttonResetData, buttonExportDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        buttonResetData = findViewById(R.id.buttonResetData);
        buttonExportDatabase = findViewById(R.id.buttonExportDatabase);

        boolean notificationsEnabled = SharedPreferencesManager.getNotificationSetting(this);
        switchNotifications.setChecked(notificationsEnabled);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesManager.setNotificationSetting(this, isChecked);
            Toast.makeText(this, "Notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });

        buttonResetData.setOnClickListener(v -> {
            SharedPreferencesManager.clearLoginData(this);
            Toast.makeText(this, "Preferences cleared. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
        });

        buttonExportDatabase.setOnClickListener(v -> showExportFormatDialog());
    }

    private void showExportFormatDialog() {
        final String[] formats = {"Plain Text (.txt)", "CSV (.csv)", "JSON (.json)", "Encrypted (.enc)"};

        new AlertDialog.Builder(this)
                .setTitle("Choose Export Format")
                .setItems(formats, (dialog, which) -> {
                    File exportFile = null;

                    switch (which) {
                        case 0:
                            exportFile = DatabaseExporter.exportAsText(this);
                            break;
                        case 1:
                            exportFile = DatabaseExporter.exportAsCsv(this);
                            break;
                        case 2:
                            exportFile = DatabaseExporter.exportAsJson(this);
                            break;
                        case 3:
                            exportFile = DatabaseExporter.exportEncrypted(this);
                            break;
                    }

                    if (exportFile != null && exportFile.exists()) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                                this,
                                getPackageName() + ".provider",
                                exportFile
                        ));
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Share Exported File"));
                    } else {
                        Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
