package com.refat.teacherszone.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudyMaterialDao {

    @Insert
    long insertMaterial(StudyMaterial material);

    @Update
    void updateMaterial(StudyMaterial material);

    @Delete
    void deleteMaterial(StudyMaterial material);

    // Get all study materials for a specific user (teacher)
    @Query("SELECT * FROM study_materials WHERE userId = :userId ORDER BY title ASC")
    List<StudyMaterial> getMaterialsForUser(int userId);

    // Get a single study material by its ID
    @Query("SELECT * FROM study_materials WHERE id = :materialId LIMIT 1")
    StudyMaterial getMaterialById(int materialId);
}