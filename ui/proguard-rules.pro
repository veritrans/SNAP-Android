# Midtrans SDK ProGuard Rules

# Keep all public API classes
-keep class com.midtrans.sdk.uikit.api.** { *; }
-keep class com.midtrans.sdk.uikit.external.** { *; }
-keep class com.midtrans.sdk.corekit.api.** { *; }

# Keep Parcelize classes
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Keep all @Parcelize annotated classes
-keep @kotlinx.parcelize.Parcelize class * { *; }
-keepclassmembers @kotlinx.parcelize.Parcelize class * {
    public static final ** CREATOR;
}

# Keep data models used for JSON serialization
-keep class com.midtrans.sdk.corekit.internal.network.model.** { *; }
-keep class com.midtrans.sdk.uikit.internal.model.** { *; }
-keep class com.midtrans.sdk.corekit.api.model.** { *; }
-keep class com.midtrans.sdk.uikit.api.model.** { *; }

# Keep @SerializedName fields for Gson
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Retrofit
-keep interface com.midtrans.sdk.corekit.internal.network.restapi.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keep class com.google.gson.stream.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Keep ViewModel classes
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class com.midtrans.sdk.uikit.internal.presentation.** { *; }

# Compose (if obfuscating Compose code causes issues)
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Dagger/Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keepclasseswithmembernames class * {
    @javax.inject.* <fields>;
    @javax.inject.* <methods>;
}

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep WebView JavaScript interfaces
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Prevent stripping of debug information
-keepattributes SourceFile,LineNumberTable

# If using coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler { *; }

# Keep custom views
-keep class com.midtrans.sdk.uikit.internal.view.** { *; }

# Payment type constants
-keep class com.midtrans.sdk.corekit.api.model.PaymentType { *; }

# Keep companion objects
-keep class * {
    public static final ** Companion;
}
-keepclassmembers class * {
    public static final ** Companion;
}