package com.refat.teacherszone.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "TeachersZonePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_SCHOOL_NAME = "schoolName";
    private static final String KEY_TEXT_SIZE = "textSize"; // New key for text size

    // Text size constants
    public static final String TEXT_SIZE_SMALL = "small";
    public static final String TEXT_SIZE_MEDIUM = "medium";
    public static final String TEXT_SIZE_LARGE = "large";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // --- Login/User related methods (existing) ---
    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setUserId(Context context, int userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt(KEY_USER_ID, -1);
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, null);
    }

    public static void setSchoolName(Context context, String schoolName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_SCHOOL_NAME, schoolName);
        editor.apply();
    }

    public static String getSchoolName(Context context) {
        return getSharedPreferences(context).getString(KEY_SCHOOL_NAME, null);
    }

    public static void clearLoginData(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        // Keep settings preferences, only clear user-specific data
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_SCHOOL_NAME);
        editor.apply();
    }

    // --- New Methods for Settings ---
    public static void setTextSize(Context context, String textSize) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_TEXT_SIZE, textSize);
        editor.apply();
    }

    public static String getTextSize(Context context) {
        // Default to Medium if no preference is set
        return getSharedPreferences(context).getString(KEY_TEXT_SIZE, TEXT_SIZE_MEDIUM);
    }
}