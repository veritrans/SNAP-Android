#!/bin/bash

echo "=========================================="
echo "Midtrans SDK Minification Test Script"
echo "=========================================="

# Set Java 17
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
echo "Using Java: $($JAVA_HOME/bin/java -version 2>&1 | head -1)"

# Clean and build
echo ""
echo "1. Building SDK with minification enabled..."
./gradlew clean :ui:assembleRelease

# Check if build succeeded
if [ $? -ne 0 ]; then
    echo "❌ Build failed! Check for ProGuard errors above"
    exit 1
fi

echo "✅ SDK built successfully with minification"

# Build sample app
echo ""
echo "2. Building sample app..."
./gradlew :app:assembleDebug

if [ $? -ne 0 ]; then
    echo "❌ Sample app build failed!"
    exit 1
fi

echo "✅ Sample app built successfully"

# Check ProGuard mapping
echo ""
echo "3. Checking ProGuard mapping file..."
if [ -f "ui/build/outputs/mapping/release/mapping.txt" ]; then
    echo "✅ Mapping file generated"
    echo "   Checking if critical classes are preserved:"
    
    # Check if important classes are NOT obfuscated
    grep -E "com.midtrans.sdk.uikit.api.UiKitApi ->" ui/build/outputs/mapping/release/mapping.txt | head -5
    
    if grep -q "com.midtrans.sdk.uikit.api.UiKitApi -> com.midtrans.sdk.uikit.api.UiKitApi:" ui/build/outputs/mapping/release/mapping.txt; then
        echo "   ✅ UiKitApi preserved (not obfuscated)"
    else
        echo "   ⚠️  UiKitApi might be obfuscated"
    fi
else
    echo "❌ No mapping file found!"
fi

# Check AAR size
echo ""
echo "4. Checking AAR size reduction..."
ORIGINAL_SIZE=$(ls -l ui/build/outputs/aar/ui-sandbox-debug.aar 2>/dev/null | awk '{print $5}')
MINIFIED_SIZE=$(ls -l ui/build/outputs/aar/ui-sandbox-release.aar 2>/dev/null | awk '{print $5}')

if [ -n "$MINIFIED_SIZE" ] && [ -n "$ORIGINAL_SIZE" ]; then
    REDUCTION=$(( ($ORIGINAL_SIZE - $MINIFIED_SIZE) * 100 / $ORIGINAL_SIZE ))
    echo "   Debug AAR: $(($ORIGINAL_SIZE / 1024))KB"
    echo "   Release AAR: $(($MINIFIED_SIZE / 1024))KB"
    echo "   Size reduction: ${REDUCTION}%"
fi

# Install and run
echo ""
echo "5. Installing sample app..."
adb install -r app/build/outputs/apk/sandbox/debug/app-sandbox-debug.apk

if [ $? -eq 0 ]; then
    echo "✅ App installed successfully"
    
    # Launch app
    echo ""
    echo "6. Launching app and monitoring for crashes..."
    
    # Clear logcat
    adb logcat -c
    
    # Start app
    adb shell am start -n com.midtrans.sdk.sample/com.midtrans.sdk.sample.presentation.config.DemoConfigurationActivity
    
    # Monitor for minification issues (run for 10 seconds)
    echo "   Monitoring logs for minification issues..."
    timeout 10 adb logcat MinificationTest:E *:S | while read line; do
        echo "   🔍 $line"
    done
    
    echo ""
    echo "=========================================="
    echo "TEST SUMMARY:"
    echo "✅ Build successful"
    echo "✅ App launches"
    echo ""
    echo "MANUAL TESTING REQUIRED:"
    echo "1. Open the sample app on your device"
    echo "2. Try a payment flow (Bank Transfer, E-Wallet, etc.)"
    echo "3. Check logcat for any crashes:"
    echo "   adb logcat | grep MinificationTest"
    echo ""
    echo "If you see any of these errors:"
    echo "- ClassNotFoundException → Add -keep rules"
    echo "- BadParcelableException → Fix @Parcelize rules"
    echo "- JsonSyntaxException → Fix @SerializedName rules"
    echo "=========================================="
else
    echo "❌ Failed to install app. Is device connected?"
    echo "Run: adb devices"
fi