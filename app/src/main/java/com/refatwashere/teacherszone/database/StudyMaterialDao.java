package com.refatwashere.teacherszone.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface StudyMaterialDao {

    @Insert
    long insertMaterial(StudyMaterial material);

    @Update
    void updateMaterial(StudyMaterial material);

    @Delete
    void deleteMaterial(StudyMaterial material);

    @Query("SELECT * FROM study_materials WHERE userId = :userId")
    List<StudyMaterial> getMaterialsForUser(int userId);

    @Query("SELECT * FROM study_materials")
    List<StudyMaterial> getAll();
}
