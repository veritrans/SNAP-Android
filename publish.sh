#!/bin/bash

# Set Java 17 for the build
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Read version from publish-variables.gradle
VERSION=$(grep "sdkVersion = " ui/publish-variables.gradle | cut -d "'" -f 2)

echo "==========================================="
echo "Publishing Midtrans SDK v${VERSION}"
echo "==========================================="
echo "Using Java: $JAVA_HOME"

echo ""
echo "Step 1: Publishing PRODUCTION version..."
echo "Building com.midtrans:uikit:${VERSION}"
./gradlew clean
./gradlew publish -PpublishVariant=production

if [ $? -eq 0 ]; then
    echo "✅ Production version published successfully"
else
    echo "❌ Failed to publish production version"
    exit 1
fi

echo ""
echo "Step 2: Publishing SANDBOX version..."
echo "Building com.midtrans:uikit:${VERSION}-SANDBOX"
./gradlew clean
./gradlew publish -PpublishVariant=sandbox

if [ $? -eq 0 ]; then
    echo "✅ Sandbox version published successfully"
else
    echo "❌ Failed to publish sandbox version"
    exit 1
fi

echo ""
echo "==========================================="
echo "✅ Both versions published successfully!"
echo "==========================================="
echo "Published artifacts:"
echo "  - com.midtrans:uikit:${VERSION}"
echo "  - com.midtrans:uikit:${VERSION}-SANDBOX"
echo ""
echo "View in Maven Central:"
echo "  https://central.sonatype.com/artifact/com.midtrans/uikit"