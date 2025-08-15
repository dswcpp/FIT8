# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Fit8 App specific rules

# Keep ALL data classes and database related classes
-keep class com.vere.fit8.data.** { *; }
-keepclassmembers class com.vere.fit8.data.** { *; }

# Keep ALL UI classes to prevent issues
-keep class com.vere.fit8.ui.** { *; }
-keepclassmembers class com.vere.fit8.ui.** { *; }

# Keep initializers (critical for data loading)
-keep class com.vere.fit8.data.initializer.** { *; }
-keepclassmembers class com.vere.fit8.data.initializer.** { *; }

# Keep Application class
-keep class com.vere.fit8.Fit8Application { *; }
-keepclassmembers class com.vere.fit8.Fit8Application { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Keep Room database classes and annotations
-keep class androidx.room.** { *; }
-keep class androidx.sqlite.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }
-keep @androidx.room.TypeConverter class * { *; }
-keep @androidx.room.TypeConverters class * { *; }

# Keep all classes with @Entity annotation
-keep @androidx.room.Entity class * {
    <fields>;
    <init>(...);
}

# Keep TypeConverters specifically
-keep class com.vere.fit8.data.converter.** { *; }

# Keep Retrofit and OkHttp classes (if used)
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# Keep Gson classes (if used)
-keep class com.google.gson.** { *; }

# Keep MPAndroidChart classes
-keep class com.github.mikephil.charting.** { *; }

# Keep Glide classes
-keep class com.bumptech.glide.** { *; }

# Keep coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep ViewBinding classes
-keep class com.vere.fit8.databinding.** { *; }

# Keep Fragment and Activity classes
-keep class com.vere.fit8.ui.** { *; }

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
