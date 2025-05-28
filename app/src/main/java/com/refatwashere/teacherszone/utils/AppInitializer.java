package com.refatwashere.teacherszone.utils;

import android.app.Application;
import android.util.Log;

import com.refatwashere.teacherszone.database.AppDatabase;

public class AppInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferencesManager.initializeFirstRun(this);

        try {
            AppDatabase.getInstance(this);
            Log.d("AppInitializer", "Database initialized successfully.");
        } catch (Exception e) {
            Log.e("AppInitializer", "Database init failed", e);
        }

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e("AppCrash", "Uncaught error in thread " + thread.getName(), throwable);
        });
    }
}
