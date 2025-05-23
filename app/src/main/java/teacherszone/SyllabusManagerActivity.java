package com.refat.teacherszone;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.refat.teacherszone.adapters.SyllabusAdapter;
import com.refat.teacherszone.database.AppDatabase;
import com.refat.teacherszone.database.Syllabus;
import com.refat.teacherszone.utils.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyllabusManagerActivity extends AppCompatActivity implements SyllabusAdapter.OnItemActionListener {

    private TextInputEditText editTextTitle, editTextCourseCode, editTextDescription, editTextFilePath;
    private Button buttonAddUpdateSyllabus, buttonClearForm;
    private RecyclerView recyclerViewSyllabi;
    private SyllabusAdapter syllabusAdapter;
    private List<Syllabus> syllabusList;
    private AppDatabase db;
    private int currentUserId; // To link syllabi to the logged-in user
    private Syllabus syllabusToEdit = null; // Holds the syllabus being edited

    // ExecutorService for background database operations
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_manager);

        // Initialize UI components
        editTextTitle = findViewById(R.id.editTextSyllabusTitle);
        editTextCourseCode = findViewById(R.id.editTextCourseCode);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextFilePath = findViewById(R.id.editTextFilePath);
        buttonAddUpdateSyllabus = findViewById(R.id.buttonAddUpdateSyllabus);
        buttonClearForm = findViewById(R.id.buttonClearForm);
        recyclerViewSyllabi = findViewById(R.id.recyclerViewSyllabi);

        // Get current user ID from SharedPreferences
        currentUserId = SharedPreferencesManager.getUserId(this);
        if (currentUserId == -1) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_LONG).show();
            finish(); // Close activity if user ID is not found
            return;
        }

        // Initialize Room Database
        db = AppDatabase.getInstance(this);

        // Setup RecyclerView
        syllabusList = new ArrayList<>();
        syllabusAdapter = new SyllabusAdapter(syllabusList, this); // 'this' refers to OnItemActionListener
        recyclerViewSyllabi.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSyllabi.setAdapter(syllabusAdapter);

        // Load existing syllabi
        loadSyllabi();

        // Set up Add/Update Button click listener
        buttonAddUpdateSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (syllabusToEdit == null) {
                    addSyllabus();
                } else {
                    updateSyllabus();
                }
            }
        });

        // Set up Clear Form Button click listener
        buttonClearForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
    }

    private void loadSyllabi() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch syllabi for the current user
                List<Syllabus> syllabi = db.syllabusDao().getSyllabiForUser(currentUserId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syllabusList.clear();
                        syllabusList.addAll(syllabi);
                        syllabusAdapter.setSyllabusList(syllabusList); // Update adapter with new list
                    }
                });
            }
        });
    }

    private void addSyllabus() {
        String title = editTextTitle.getText().toString().trim();
        String courseCode = editTextCourseCode.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String filePath = editTextFilePath.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(courseCode) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill in title, course code, and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Syllabus newSyllabus = new Syllabus(currentUserId, title, courseCode, description, filePath);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = db.syllabusDao().insertSyllabus(newSyllabus);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (id > 0) {
                            Toast.makeText(SyllabusManagerActivity.this, "Syllabus added successfully!", Toast.LENGTH_SHORT).show();
                            clearForm();
                            loadSyllabi(); // Refresh the list
                        } else {
                            Toast.makeText(SyllabusManagerActivity.this, "Failed to add syllabus.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void updateSyllabus() {
        if (syllabusToEdit == null) return;

        String title = editTextTitle.getText().toString().trim();
        String courseCode = editTextCourseCode.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String filePath = editTextFilePath.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(courseCode) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        syllabusToEdit.setTitle(title);
        syllabusToEdit.setCourseCode(courseCode);
        syllabusToEdit.setDescription(description);
        syllabusToEdit.setFilePath(filePath);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                db.syllabusDao().updateSyllabus(syllabusToEdit);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SyllabusManagerActivity.this, "Syllabus updated successfully!", Toast.LENGTH_SHORT).show();
                        clearForm();
                        loadSyllabi(); // Refresh the list
                    }
                });
            }
        });
    }

    private void deleteSyllabus(Syllabus syllabus) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Syllabus")
                .setMessage("Are you sure you want to delete this syllabus?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.syllabusDao().deleteSyllabus(syllabus);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SyllabusManagerActivity.this, "Syllabus deleted.", Toast.LENGTH_SHORT).show();
                                    loadSyllabi(); // Refresh the list
                                    clearForm(); // Clear form if the deleted item was being edited
                                }
                            });
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearForm() {
        editTextTitle.setText("");
        editTextCourseCode.setText("");
        editTextDescription.setText("");
        editTextFilePath.setText("");
        buttonAddUpdateSyllabus.setText("Add Syllabus");
        syllabusToEdit = null;
    }

    @Override
    public void onEditClick(Syllabus syllabus) {
        // Populate the form fields with the syllabus data for editing
        editTextTitle.setText(syllabus.getTitle());
        editTextCourseCode.setText(syllabus.getCourseCode());
        editTextDescription.setText(syllabus.getDescription());
        editTextFilePath.setText(syllabus.getFilePath());
        buttonAddUpdateSyllabus.setText("Update Syllabus");
        syllabusToEdit = syllabus; // Set the syllabus to be edited
    }

    @Override
    public void onDeleteClick(Syllabus syllabus) {
        deleteSyllabus(syllabus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Shut down the executor service
    }
}