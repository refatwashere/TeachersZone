package com.refatwashere.teacherszone.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_materials")
public class StudyMaterial {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String title;
    private String subject;
    private String materialType;
    private String contentPath;
    private String notes;

    public StudyMaterial(int userId, String title, String subject, String materialType, String contentPath, String notes) {
        this.userId = userId;
        this.title = title;
        this.subject = subject;
        this.materialType = materialType;
        this.contentPath = contentPath;
        this.notes = notes;
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getMaterialType() { return materialType; }
    public String getContentPath() { return contentPath; }
    public String getNotes() { return notes; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }
    public void setContentPath(String contentPath) { this.contentPath = contentPath; }
    public void setNotes(String notes) { this.notes = notes; }
}
