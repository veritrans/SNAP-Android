# Sample App ProGuard Rules
# Note: This file is ONLY for the sample app's own classes
# Real SDK users won't need these rules - consumer rules handle everything

# Keep sample app classes
-keep class com.midtrans.sdk.sample.** { *; }

# Debugging
-keepattributes SourceFile,LineNumberTable