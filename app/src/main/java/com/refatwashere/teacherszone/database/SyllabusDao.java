package com.refatwashere.teacherszone.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface SyllabusDao {

    @Insert
    long insertSyllabus(Syllabus syllabus);

    @Update
    void updateSyllabus(Syllabus syllabus);

    @Delete
    void deleteSyllabus(Syllabus syllabus);

    @Query("SELECT * FROM syllabi WHERE userId = :userId")
    List<Syllabus> getSyllabiForUser(int userId);

    @Query("SELECT * FROM syllabi WHERE id = :id")
    Syllabus getSyllabusById(int id);

    @Query("SELECT * FROM syllabi")
    List<Syllabus> getAllSyllabi();
}
