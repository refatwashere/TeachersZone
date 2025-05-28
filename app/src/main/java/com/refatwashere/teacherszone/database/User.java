package com.refatwashere.teacherszone.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String email;
    private String password;
    private String schoolName;

    public User(String email, String password, String schoolName) {
        this.email = email;
        this.password = password;
        this.schoolName = schoolName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSchoolName() {
        return schoolName;
    }
}
