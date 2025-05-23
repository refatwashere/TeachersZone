package com.refat.teacherszone.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Define this class as a Room Entity and specify the table name
@Entity(tableName = "users")
public class User {

    // Define the primary key for the table, auto-generating IDs
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Define columns for user data
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password; // Store hashed passwords in a real app! For simplicity, plain text here.

    @ColumnInfo(name = "school_name")
    private String schoolName;

    // Constructor to create a new User object (excluding ID, which is auto-generated)
    public User(String email, String password, String schoolName) {
        this.email = email;
        this.password = password;
        this.schoolName = schoolName;
    }

    // --- Getters and Setters for all fields ---
    // Room uses getters and setters to access and modify data

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}