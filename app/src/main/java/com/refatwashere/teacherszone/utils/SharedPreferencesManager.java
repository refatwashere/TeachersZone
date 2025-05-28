package com.refatwashere.teacherszone.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "TeachersZonePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_SCHOOL_NAME = "schoolName";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notificationsEnabled";
    private static final String KEY_INITIALIZED = "initialized";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void initializeFirstRun(Context context) {
        SharedPreferences prefs = getPrefs(context);
        if (!prefs.contains(KEY_INITIALIZED)) {
            prefs.edit()
                    .putBoolean(KEY_IS_LOGGED_IN, false)
                    .putBoolean(KEY_INITIALIZED, true)
                    .apply();
        }
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        getPrefs(context).edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setUserId(Context context, int userId) {
        getPrefs(context).edit().putInt(KEY_USER_ID, userId).apply();
    }

    public static int getUserId(Context context) {
        return getPrefs(context).getInt(KEY_USER_ID, -1);
    }

    public static void setUserEmail(Context context, String email) {
        getPrefs(context).edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(KEY_USER_EMAIL, "");
    }

    public static void setSchoolName(Context context, String schoolName) {
        getPrefs(context).edit().putString(KEY_SCHOOL_NAME, schoolName).apply();
    }

    public static String getSchoolName(Context context) {
        return getPrefs(context).getString(KEY_SCHOOL_NAME, "");
    }

    public static void setNotificationSetting(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }

    public static boolean getNotificationSetting(Context context) {
        return getPrefs(context).getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    public static void clearLoginData(Context context) {
        getPrefs(context).edit()
                .remove(KEY_IS_LOGGED_IN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_EMAIL)
                .remove(KEY_SCHOOL_NAME)
                .apply();
    }
}
