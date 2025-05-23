package com.refat.teacherszone.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

// Define this as a Room DAO
@Dao
public interface UserDao {

    // Insert a new user into the database. Returns the row ID of the newly inserted user.
    @Insert
    long insertUser(User user);

    // Query to get a user by email (for checking if a user already exists)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    // Query to get a user by email and password (for login)
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User getUserByEmailAndPassword(String email, String password);

    // You can add more DAO methods as needed, e.g., to update user profiles, delete users etc.
}