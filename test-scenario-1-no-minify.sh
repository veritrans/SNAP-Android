#!/bin/bash

echo "==========================================="
echo "SCENARIO 1: Testing Non-Minified User App"
echo "==========================================="
echo "This simulates a user who doesn't enable minification"
echo ""

# Set Java 17
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home

# 1. Build minified SDK (what you distribute)
echo "Step 1: Building minified SDK..."
./gradlew :ui:assembleSandboxRelease
if [ $? -ne 0 ]; then
    echo "❌ SDK build failed"
    exit 1
fi
echo "✅ SDK built with minification"
echo ""

# 2. Build app WITHOUT minification
echo "Step 2: Building app WITHOUT minification..."
sed -i '' 's/isMinifyEnabled = true/isMinifyEnabled = false/' app/build.gradle.kts
./gradlew :app:assembleSandboxDebug

if [ $? -ne 0 ]; then
    echo "❌ App build failed"
    exit 1
fi
echo "✅ App built without minification"
echo ""

# 3. Install and launch
echo "Step 3: Installing app..."
adb uninstall com.midtrans.sdk.sample 2>/dev/null
adb install app/build/outputs/apk/sandbox/debug/app-sandbox-debug.apk

if [ $? -eq 0 ]; then
    echo "✅ App installed successfully"
    echo ""
    echo "Launching app..."
    adb shell am start -n com.midtrans.sdk.sample/com.midtrans.sdk.sample.presentation.config.DemoConfigurationActivity
    sleep 3
    
    if adb shell pidof com.midtrans.sdk.sample > /dev/null; then
        echo ""
        echo "==========================================="
        echo "✅ SCENARIO 1 READY FOR TESTING"
        echo "==========================================="
        echo "App is running WITHOUT minification"
        echo ""
        echo "Please test:"
        echo "1. Launch Demo App - Test payment flow"
        echo "2. Launch Demo App Legacy - Test legacy flow"
        echo "3. Try different payment methods"
        echo "==========================================="
    else
        echo "⚠️ App might be slow to start, please check manually"
    fi
else
    echo "❌ Failed to install app"
    exit 1
fi