plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android-extensions")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = com.gtf.snap.Dependencies.composeVersion
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation(com.gtf.snap.ComposeUiLibraries.compose)
    implementation(com.gtf.snap.ComposeUiLibraries.composeTool)
    implementation(com.gtf.snap.ComposeUiLibraries.composeMaterial)
    implementation(com.gtf.snap.ComposeUiLibraries.composeFoundation)
    implementation(com.gtf.snap.ComposeUiLibraries.composeActivity)
//    implementation(com.gtf.snap.ComposeUiLibraries.composeFramework)

    implementation(com.gtf.snap.ComposeUiLibraries.composeToolingPreview)
    implementation(com.gtf.snap.ComposeUiLibraries.composeLayout)
    implementation(com.gtf.snap.ComposeUiLibraries.composeRuntime)
    implementation(com.gtf.snap.ComposeUiLibraries.composeGoogleFont)
    implementation(com.gtf.snap.ComposeUiLibraries.composeLiveData)
    implementation(com.gtf.snap.ComposeUiLibraries.composeConstraintLayout)
    implementation(com.gtf.snap.ComposeUiLibraries.composeAnimatedDrawable)
    implementation(com.gtf.snap.ComposeUiLibraries.composeAnimation)

    debugImplementation("androidx.customview:customview:1.2.0-alpha01")
    debugImplementation("androidx.customview:customview-poolingcontainer:1.0.0-alpha01")
    implementation(project(":corekit"))
}