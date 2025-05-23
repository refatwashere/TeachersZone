package com.refat.teacherszone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Update entities array and increment the version
@Database(entities = {User.class, Syllabus.class, StudyMaterial.class}, version = 3, exportSchema = false) // VERSION CHANGED TO 3
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract SyllabusDao syllabusDao();
    public abstract StudyMaterialDao studyMaterialDao(); // Add this abstract method for StudyMaterialDao

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "teacherszone_database")
                            .fallbackToDestructiveMigration() // Still important for development
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}