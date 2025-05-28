package com.refatwashere.teacherszone;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.refatwashere.teacherszone.adapters.SyllabusAdapter;
import com.refatwashere.teacherszone.database.AppDatabase;
import com.refatwashere.teacherszone.database.Syllabus;
import com.refatwashere.teacherszone.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class SyllabusActivity extends AppCompatActivity implements SyllabusAdapter.OnItemActionListener {

    private EditText editTitle, editCourseCode, editDescription;
    private Button buttonAddUpdate, buttonClear;
    private RecyclerView recyclerView;

    private AppDatabase db;
    private int userId;
    private SyllabusAdapter adapter;
    private List<Syllabus> syllabusList;
    private Syllabus editingSyllabus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        editTitle = findViewById(R.id.editTextSyllabusTitle);
        editCourseCode = findViewById(R.id.editTextCourseCode);
        editDescription = findViewById(R.id.editTextDescription);
        buttonAddUpdate = findViewById(R.id.buttonAddUpdateSyllabus);
        buttonClear = findViewById(R.id.buttonClearSyllabusForm);
        recyclerView = findViewById(R.id.recyclerViewSyllabi);

        db = AppDatabase.getInstance(this);
        userId = SharedPreferencesManager.getUserId(this);

        syllabusList = new ArrayList<>();
        adapter = new SyllabusAdapter(syllabusList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadSyllabi();

        buttonAddUpdate.setOnClickListener(v -> {
            if (editingSyllabus == null) {
                addSyllabus();
            } else {
                updateSyllabus();
            }
        });

        buttonClear.setOnClickListener(v -> clearForm());
    }

    private void loadSyllabi() {
        new Thread(() -> {
            List<Syllabus> loaded = db.syllabusDao().getSyllabiForUser(userId);
            runOnUiThread(() -> {
                syllabusList.clear();
                syllabusList.addAll(loaded);
                adapter.setSyllabusList(syllabusList);
            });
        }).start();
    }

    private void addSyllabus() {
        String title = editTitle.getText().toString().trim();
        String courseCode = editCourseCode.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(courseCode)) {
            Toast.makeText(this, "Fill in title and course code", Toast.LENGTH_SHORT).show();
            return;
        }

        Syllabus s = new Syllabus(userId, title, courseCode, description);

        new Thread(() -> {
            long id = db.syllabusDao().insertSyllabus(s);
            runOnUiThread(() -> {
                if (id > 0) {
                    Toast.makeText(this, "Syllabus added", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadSyllabi();
                }
            });
        }).start();
    }

    private void updateSyllabus() {
        String title = editTitle.getText().toString().trim();
        String courseCode = editCourseCode.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(courseCode)) {
            Toast.makeText(this, "Fill in title and course code", Toast.LENGTH_SHORT).show();
            return;
        }

        editingSyllabus.setTitle(title);
        editingSyllabus.setCourseCode(courseCode);
        editingSyllabus.setDescription(description);

        new Thread(() -> {
            db.syllabusDao().updateSyllabus(editingSyllabus);
            runOnUiThread(() -> {
                Toast.makeText(this, "Syllabus updated", Toast.LENGTH_SHORT).show();
                clearForm();
                loadSyllabi();
            });
        }).start();
    }

    private void deleteSyllabus(Syllabus s) {
        new Thread(() -> {
            db.syllabusDao().deleteSyllabus(s);
            runOnUiThread(() -> {
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                clearForm();
                loadSyllabi();
            });
        }).start();
    }

    private void clearForm() {
        editTitle.setText("");
        editCourseCode.setText("");
        editDescription.setText("");
        editingSyllabus = null;
        buttonAddUpdate.setText("Add");
    }

    @Override
    public void onEditClick(Syllabus s) {
        editingSyllabus = s;
        editTitle.setText(s.getTitle());
        editCourseCode.setText(s.getCourseCode());
        editDescription.setText(s.getDescription());
        buttonAddUpdate.setText("Update");
    }

    @Override
    public void onDeleteClick(Syllabus s) {
        deleteSyllabus(s);
    }
}
