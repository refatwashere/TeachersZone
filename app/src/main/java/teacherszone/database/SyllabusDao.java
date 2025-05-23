package com.refat.teacherszone.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SyllabusDao {

    // Insert a new syllabus
    @Insert
    long insertSyllabus(Syllabus syllabus);

    // Update an existing syllabus
    @Update
    void updateSyllabus(Syllabus syllabus);

    // Delete a syllabus
    @Delete
    void deleteSyllabus(Syllabus syllabus);

    // Get all syllabi for a specific user (teacher)
    @Query("SELECT * FROM syllabi WHERE userId = :userId ORDER BY title ASC")
    List<Syllabus> getSyllabiForUser(int userId);

    // Get a single syllabus by its ID
    @Query("SELECT * FROM syllabi WHERE id = :syllabusId LIMIT 1")
    Syllabus getSyllabusById(int syllabusId);
}