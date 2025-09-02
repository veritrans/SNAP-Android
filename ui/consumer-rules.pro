# Midtrans SDK Consumer ProGuard Rules
# These rules are automatically applied when users enable minification in their apps

# Keep ALL Midtrans SDK classes (safest approach for SDK consumers)
-keep class com.midtrans.sdk.** { *; }

# Keep Parcelize classes
-keep @kotlinx.parcelize.Parcelize class * { *; }
-keepclassmembers @kotlinx.parcelize.Parcelize class * {
    public static final ** CREATOR;
}

# Keep @SerializedName fields for Gson
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Retrofit
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson
-keep class com.google.gson.stream.** { *; }