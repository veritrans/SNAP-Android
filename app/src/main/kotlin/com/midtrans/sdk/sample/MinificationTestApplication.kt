package com.midtrans.sdk.sample

import android.app.Application
import android.os.BadParcelableException
import android.util.Log

class MinificationTestApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Set up crash detection for minification issues
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Log.e("MinificationTest", "===========================================")
            Log.e("MinificationTest", "CRASH DETECTED on thread: ${thread.name}", exception)
            Log.e("MinificationTest", "===========================================")
            
            // Identify specific minification-related issues
            when (exception) {
                is ClassNotFoundException -> {
                    Log.e("MinificationTest", "❌ CLASS NOT FOUND - Likely obfuscated: ${exception.message}")
                    Log.e("MinificationTest", "Add to ProGuard: -keep class ${exception.message?.substringBefore(' ')} { *; }")
                }
                is NoSuchMethodException -> {
                    Log.e("MinificationTest", "❌ METHOD NOT FOUND - Likely removed/obfuscated: ${exception.message}")
                    Log.e("MinificationTest", "Check ProGuard rules for this method")
                }
                is NoSuchFieldException -> {
                    Log.e("MinificationTest", "❌ FIELD NOT FOUND - Likely removed/obfuscated: ${exception.message}")
                    Log.e("MinificationTest", "Add @Keep annotation or ProGuard rule")
                }
                is BadParcelableException -> {
                    Log.e("MinificationTest", "❌ PARCELABLE ISSUE - Parcelize broken: ${exception.message}")
                    Log.e("MinificationTest", "Check @Parcelize keep rules")
                }
                is IllegalArgumentException -> {
                    if (exception.message?.contains("Unable to create converter") == true) {
                        Log.e("MinificationTest", "❌ RETROFIT CONVERTER ISSUE - API interface obfuscated")
                        Log.e("MinificationTest", "Add: -keep interface * { @retrofit2.http.* <methods>; }")
                    } else if (exception.message?.contains("JsonSyntax") == true) {
                        Log.e("MinificationTest", "❌ JSON PARSING FAILED - Fields likely renamed")
                        Log.e("MinificationTest", "Check @SerializedName annotations")
                    }
                }
            }
            
            // Check if it's related to Midtrans SDK
            exception.stackTrace.forEach { element ->
                if (element.className.contains("com.midtrans")) {
                    Log.e("MinificationTest", "📍 Midtrans SDK involved: ${element.className}.${element.methodName}")
                }
            }
            
            // Rethrow to maintain default behavior
            Thread.getDefaultUncaughtExceptionHandler()?.uncaughtException(thread, exception)
        }
        
        // Test if critical SDK classes are accessible
        testSdkClassesAccessible()
    }
    
    private fun testSdkClassesAccessible() {
        try {
            // Test if main SDK classes can be loaded
            val classes = listOf(
                "com.midtrans.sdk.uikit.api.UiKitApi",
                "com.midtrans.sdk.uikit.api.model.TransactionResult",
                "com.midtrans.sdk.corekit.api.model.TransactionResponse",
                "com.midtrans.sdk.uikit.internal.model.CustomerInfo"
            )
            
            classes.forEach { className ->
                try {
                    Class.forName(className)
                    Log.d("MinificationTest", "✅ Class accessible: $className")
                } catch (e: ClassNotFoundException) {
                    Log.e("MinificationTest", "❌ Class NOT accessible: $className", e)
                }
            }
        } catch (e: Exception) {
            Log.e("MinificationTest", "Error testing SDK classes", e)
        }
    }
}