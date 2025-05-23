package com.refat.teacherszone.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Define this class as a Room Entity and specify the table name
@Entity(tableName = "syllabi",
        // Define a foreign key relationship with the User table
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id", // The ID column in the User table
                childColumns = "userId", // The column in this table that refers to the User ID
                onDelete = ForeignKey.CASCADE) // If a user is deleted, their syllabi are also deleted
)
public class Syllabus {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userId")
    private int userId; // Foreign key to link syllabus to a specific teacher/user

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "course_code")
    private String courseCode;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "file_path") // To simulate file uploads, we'll store a path or a dummy value
    private String filePath;

    // Constructor (excluding ID, which is auto-generated)
    public Syllabus(int userId, String title, String courseCode, String description, String filePath) {
        this.userId = userId;
        this.title = title;
        this.courseCode = courseCode;
        this.description = description;
        this.filePath = filePath;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}