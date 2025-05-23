package com.refat.teacherszone.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_materials",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE)
)
public class StudyMaterial {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userId")
    private int userId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "subject")
    private String subject; // e.g., Math, Science, History

    @ColumnInfo(name = "material_type") // e.g., Link, Document, Video, Notes
    private String materialType;

    @ColumnInfo(name = "content_path") // URL or local file path
    private String contentPath;

    @ColumnInfo(name = "notes") // Additional notes about the material
    private String notes;

    // Constructor
    public StudyMaterial(int userId, String title, String subject, String materialType, String contentPath, String notes) {
        this.userId = userId;
        this.title = title;
        this.subject = subject;
        this.materialType = materialType;
        this.contentPath = contentPath;
        this.notes = notes;
    }

    // --- Getters and Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}