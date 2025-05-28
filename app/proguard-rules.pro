# Keep Room database entities, DAOs, and models
-keep class androidx.room.** { *; }
-keep class com.refat.teacherszone.database.** { *; }

# Keep all model classes with Room annotations
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# Prevent obfuscation of Room queries (annotations)
-keepattributes *Annotation*

# Keep Gson or JSON parsing (if you use it)
-keep class com.google.gson.** { *; }
-keepattributes Signature

# Keep all activities, fragments, viewmodels
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends androidx.fragment.app.Fragment

# Keep file provider
-keep public class androidx.core.content.FileProvider { *; }

# Keep material components (avoid crashing styles)
-keep class com.google.android.material.** { *; }

# Log and debugging (can be removed in production)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Optional: Keep retrofit or okhttp if you add network (not used yet)
# -keep class retrofit2.** { *; }
# -keep class okhttp3.** { *; }

# General recommended
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.**
-dontwarn sun.misc.**

