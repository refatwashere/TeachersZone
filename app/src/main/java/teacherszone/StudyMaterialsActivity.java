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

import com.refat.teacherszone.adapters.StudyMaterialAdapter;
import com.refat.teacherszone.database.AppDatabase;
import com.refat.teacherszone.database.StudyMaterial;
import com.refat.teacherszone.utils.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyMaterialsActivity extends AppCompatActivity implements StudyMaterialAdapter.OnItemActionListener {

    private TextInputEditText editTextTitle, editTextSubject, editTextMaterialType, editTextContentPath, editTextNotes;
    private Button buttonAddUpdateMaterial, buttonClearMaterialForm;
    private RecyclerView recyclerViewMaterials;
    private StudyMaterialAdapter materialAdapter;
    private List<StudyMaterial> materialList;
    private AppDatabase db;
    private int currentUserId;
    private StudyMaterial materialToEdit = null;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_materials);

        // Initialize UI components
        editTextTitle = findViewById(R.id.editTextMaterialTitle);
        editTextSubject = findViewById(R.id.editTextMaterialSubject);
        editTextMaterialType = findViewById(R.id.editTextMaterialType);
        editTextContentPath = findViewById(R.id.editTextContentPath);
        editTextNotes = findViewById(R.id.editTextNotes);
        buttonAddUpdateMaterial = findViewById(R.id.buttonAddUpdateMaterial);
        buttonClearMaterialForm = findViewById(R.id.buttonClearMaterialForm);
        recyclerViewMaterials = findViewById(R.id.recyclerViewMaterials);

        // Get current user ID
        currentUserId = SharedPreferencesManager.getUserId(this);
        if (currentUserId == -1) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize Room Database
        db = AppDatabase.getInstance(this);

        // Setup RecyclerView
        materialList = new ArrayList<>();
        materialAdapter = new StudyMaterialAdapter(materialList, this);
        recyclerViewMaterials.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMaterials.setAdapter(materialAdapter);

        // Load existing materials
        loadMaterials();

        // Set up Add/Update Button click listener
        buttonAddUpdateMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialToEdit == null) {
                    addMaterial();
                } else {
                    updateMaterial();
                }
            }
        });

        // Set up Clear Form Button click listener
        buttonClearMaterialForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
    }

    private void loadMaterials() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<StudyMaterial> materials = db.studyMaterialDao().getMaterialsForUser(currentUserId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        materialList.clear();
                        materialList.addAll(materials);
                        materialAdapter.setMaterialList(materialList);
                    }
                });
            }
        });
    }

    private void addMaterial() {
        String title = editTextTitle.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String type = editTextMaterialType.getText().toString().trim();
        String contentPath = editTextContentPath.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(type) || TextUtils.isEmpty(contentPath)) {
            Toast.makeText(this, "Please fill in title, subject, type, and content path", Toast.LENGTH_SHORT).show();
            return;
        }

        StudyMaterial newMaterial = new StudyMaterial(currentUserId, title, subject, type, contentPath, notes);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = db.studyMaterialDao().insertMaterial(newMaterial);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (id > 0) {
                            Toast.makeText(StudyMaterialsActivity.this, "Material added successfully!", Toast.LENGTH_SHORT).show();
                            clearForm();
                            loadMaterials();
                        } else {
                            Toast.makeText(StudyMaterialsActivity.this, "Failed to add material.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void updateMaterial() {
        if (materialToEdit == null) return;

        String title = editTextTitle.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String type = editTextMaterialType.getText().toString().trim();
        String contentPath = editTextContentPath.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(type) || TextUtils.isEmpty(contentPath)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        materialToEdit.setTitle(title);
        materialToEdit.setSubject(subject);
        materialToEdit.setMaterialType(type);
        materialToEdit.setContentPath(contentPath);
        materialToEdit.setNotes(notes);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                db.studyMaterialDao().updateMaterial(materialToEdit);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StudyMaterialsActivity.this, "Material updated successfully!", Toast.LENGTH_SHORT).show();
                        clearForm();
                        loadMaterials();
                    }
                });
            }
        });
    }

    private void deleteMaterial(StudyMaterial material) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Study Material")
                .setMessage("Are you sure you want to delete this study material?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.studyMaterialDao().deleteMaterial(material);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(StudyMaterialsActivity.this, "Study material deleted.", Toast.LENGTH_SHORT).show();
                                    loadMaterials();
                                    clearForm();
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
        editTextSubject.setText("");
        editTextMaterialType.setText("");
        editTextContentPath.setText("");
        editTextNotes.setText("");
        buttonAddUpdateMaterial.setText("Add Material");
        materialToEdit = null;
    }

    @Override
    public void onEditClick(StudyMaterial material) {
        // Populate the form fields with the material data for editing
        editTextTitle.setText(material.getTitle());
        editTextSubject.setText(material.getSubject());
        editTextMaterialType.setText(material.getMaterialType());
        editTextContentPath.setText(material.getContentPath());
        editTextNotes.setText(material.getNotes());
        buttonAddUpdateMaterial.setText("Update Material");
        materialToEdit = material;
    }

    @Override
    public void onDeleteClick(StudyMaterial material) {
        deleteMaterial(material);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}