package com.refatwashere.teacherszone.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "syllabi")
public class Syllabus {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String title;
    private String courseCode;
    private String description;

    public Syllabus(int userId, String title, String courseCode, String description) {
        this.userId = userId;
        this.title = title;
        this.courseCode = courseCode;
        this.description = description;
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getCourseCode() { return courseCode; }
    public String getDescription() { return description; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setDescription(String description) { this.description = description; }
}
