package com.refatwashere.teacherszone;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.refatwashere.teacherszone.adapters.StudyMaterialAdapter;
import com.refatwashere.teacherszone.database.AppDatabase;
import com.refatwashere.teacherszone.database.StudyMaterial;
import com.refatwashere.teacherszone.utils.SharedPreferencesManager;
import com.refatwashere.teacherszone.utils.TextUtilsHelper;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterialsActivity extends AppCompatActivity implements StudyMaterialAdapter.OnItemActionListener {

    private TextInputEditText editTitle, editSubject, editType, editPath, editNotes;
    private Button buttonAddUpdate, buttonClear;
    private RecyclerView recyclerView;

    private AppDatabase db;
    private int userId;
    private StudyMaterialAdapter adapter;
    private List<StudyMaterial> materials;
    private StudyMaterial editingMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_materials);

        editTitle = findViewById(R.id.editTextMaterialTitle);
        editSubject = findViewById(R.id.editTextSubject);
        editType = findViewById(R.id.editTextMaterialType);
        editPath = findViewById(R.id.editTextContentPath);
        editNotes = findViewById(R.id.editTextNotes);
        buttonAddUpdate = findViewById(R.id.buttonAddUpdateMaterial);
        buttonClear = findViewById(R.id.buttonClearMaterialForm);
        recyclerView = findViewById(R.id.recyclerViewMaterials);

        db = AppDatabase.getInstance(this);
        userId = SharedPreferencesManager.getUserId(this);

        materials = new ArrayList<>();
        adapter = new StudyMaterialAdapter(materials, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadMaterials();

        buttonAddUpdate.setOnClickListener(v -> {
            if (editingMaterial == null) {
                addMaterial();
            } else {
                updateMaterial();
            }
        });

        buttonClear.setOnClickListener(v -> clearForm());
    }

    private void loadMaterials() {
        new Thread(() -> {
            List<StudyMaterial> loadedMaterials = db.studyMaterialDao().getMaterialsForUser(userId);
            runOnUiThread(() -> {
                materials.clear();
                materials.addAll(loadedMaterials);
                adapter.setMaterialList(materials);
            });
        }).start();
    }

    private void addMaterial() {
        String title = TextUtilsHelper.getSafeText(editTitle);
        String subject = TextUtilsHelper.getSafeText(editSubject);
        String type = TextUtilsHelper.getSafeText(editType);
        String path = TextUtilsHelper.getSafeText(editPath);
        String notes = TextUtilsHelper.getSafeText(editNotes);

        if (title.isEmpty() || subject.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill in title, subject, and type", Toast.LENGTH_SHORT).show();
            return;
        }

        StudyMaterial material = new StudyMaterial(userId, title, subject, type, path, notes);

        new Thread(() -> {
            long id = db.studyMaterialDao().insertMaterial(material);
            runOnUiThread(() -> {
                if (id > 0) {
                    Toast.makeText(this, "Material added", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadMaterials();
                } else {
                    Toast.makeText(this, "Failed to add material", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void updateMaterial() {
        String title = TextUtilsHelper.getSafeText(editTitle);
        String subject = TextUtilsHelper.getSafeText(editSubject);
        String type = TextUtilsHelper.getSafeText(editType);
        String path = TextUtilsHelper.getSafeText(editPath);
        String notes = TextUtilsHelper.getSafeText(editNotes);

        if (title.isEmpty() || subject.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill in title, subject, and type", Toast.LENGTH_SHORT).show();
            return;
        }

        editingMaterial.setTitle(title);
        editingMaterial.setSubject(subject);
        editingMaterial.setMaterialType(type);
        editingMaterial.setContentPath(path);
        editingMaterial.setNotes(notes);

        new Thread(() -> {
            db.studyMaterialDao().updateMaterial(editingMaterial);
            runOnUiThread(() -> {
                Toast.makeText(this, "Material updated", Toast.LENGTH_SHORT).show();
                clearForm();
                loadMaterials();
            });
        }).start();
    }

    private void deleteMaterial(StudyMaterial material) {
        new Thread(() -> {
            db.studyMaterialDao().deleteMaterial(material);
            runOnUiThread(() -> {
                Toast.makeText(this, "Material deleted", Toast.LENGTH_SHORT).show();
                clearForm();
                loadMaterials();
            });
        }).start();
    }

    private void clearForm() {
        editTitle.setText("");
        editSubject.setText("");
        editType.setText("");
        editPath.setText("");
        editNotes.setText("");
        editingMaterial = null;
        buttonAddUpdate.setText("Add");
    }

    @Override
    public void onEditClick(StudyMaterial material) {
        editingMaterial = material;
        editTitle.setText(material.getTitle());
        editSubject.setText(material.getSubject());
        editType.setText(material.getMaterialType());
        editPath.setText(material.getContentPath());
        editNotes.setText(material.getNotes());
        buttonAddUpdate.setText("Update");
    }

    @Override
    public void onDeleteClick(StudyMaterial material) {
        deleteMaterial(material);
    }
}
