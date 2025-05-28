package com.refatwashere.teacherszone.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.refatwashere.teacherszone.database.AppDatabase;
import com.refatwashere.teacherszone.database.StudyMaterial;
import com.refatwashere.teacherszone.database.Syllabus;
import com.refatwashere.teacherszone.database.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DatabaseExporter {

    private static final String TAG = "DatabaseExporter";
    private static final String ENCRYPTION_KEY = "1234567890abcdef"; // dev-only (16 bytes)

    public static File exportAsText(Context ctx) {
        File file = new File(ctx.getExternalFilesDir(null), "teacherszone_export.txt");
        try (FileWriter w = new FileWriter(file)) {
            AppDatabase db = AppDatabase.getInstance(ctx);
            w.write("USERS:\n");
            for (User u : db.userDao().getAllUsers()) {
                w.write(u.getId() + " | " + u.getEmail() + " | " + u.getSchoolName() + "\n");
            }
            w.write("\nSYLLABI:\n");
            for (Syllabus s : db.syllabusDao().getAllSyllabi()) {
                w.write(s.getId() + " | " + s.getTitle() + " | " + s.getCourseCode() + "\n");
            }
            w.write("\nMATERIALS:\n");
            for (StudyMaterial m : db.studyMaterialDao().getAll()) {
                w.write(m.getId() + " | " + m.getTitle() + " | " + m.getSubject() + "\n");
            }
        } catch (Exception e) {
            Log.e(TAG, "Text export failed", e);
        }
        return file;
    }

    public static File exportAsCsv(Context ctx) {
        File file = new File(ctx.getExternalFilesDir(null), "teacherszone_export.csv");
        try (FileWriter w = new FileWriter(file)) {
            AppDatabase db = AppDatabase.getInstance(ctx);
            w.write("UserID,Email,School\n");
            for (User u : db.userDao().getAllUsers()) {
                w.write(u.getId() + "," + u.getEmail() + "," + u.getSchoolName() + "\n");
            }
        } catch (Exception e) {
            Log.e(TAG, "CSV export failed", e);
        }
        return file;
    }

    public static File exportAsJson(Context ctx) {
        File file = new File(ctx.getExternalFilesDir(null), "teacherszone_export.json");
        try (FileWriter w = new FileWriter(file)) {
            AppDatabase db = AppDatabase.getInstance(ctx);
            JSONArray usersArray = new JSONArray();
            for (User u : db.userDao().getAllUsers()) {
                JSONObject o = new JSONObject();
                o.put("id", u.getId());
                o.put("email", u.getEmail());
                o.put("school", u.getSchoolName());
                usersArray.put(o);
            }
            JSONObject root = new JSONObject();
            root.put("users", usersArray);
            w.write(root.toString(2));
        } catch (Exception e) {
            Log.e(TAG, "JSON export failed", e);
        }
        return file;
    }

    public static File exportEncrypted(Context ctx) {
        File file = new File(ctx.getExternalFilesDir(null), "teacherszone_export.enc");
        try {
            StringBuilder content = new StringBuilder();
            AppDatabase db = AppDatabase.getInstance(ctx);
            for (User u : db.userDao().getAllUsers()) {
                content.append(u.getId()).append("|").append(u.getEmail()).append("|").append(u.getSchoolName()).append("\n");
            }

            byte[] keyBytes = ENCRYPTION_KEY.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(content.toString().getBytes());

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(encrypted);
            }
        } catch (Exception e) {
            Log.e(TAG, "Encrypted export failed", e);
        }
        return file;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptExport(Context ctx, File file) {
        try {
            byte[] keyBytes = ENCRYPTION_KEY.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] encrypted = java.nio.file.Files.readAllBytes(file.toPath());
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);
        } catch (Exception e) {
            Log.e(TAG, "Decryption failed", e);
            return null;
        }
    }
}
