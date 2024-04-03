
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
}

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.midtrans.sdk.sample"
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 5
        versionName = "2.1.0"
    }

    signingConfigs {
        create("release") {
            keyAlias = project.findProperty("keyAlias")?.toString()
            keyPassword = project.findProperty("keyPassword")?.toString()
            storeFile = file("keystorefile.jks")
            storePassword = project.findProperty("keystorePassword")?.toString()
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        flavorDimensions.add("env")
        productFlavors {
            create("sandbox") {
                dimension = "env"
                matchingFallbacks += "sandbox"
            }
            create("production") {
                dimension = "env"
                matchingFallbacks += "production"
            }
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
        kotlinCompilerExtensionVersion = com.gtf.snap.Dependencies.composeCompilerVersion
    }


    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.midtrans.sdk.sample"

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation(com.gtf.snap.CommonLibraries.appCompat)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation(com.gtf.snap.ComposeUiLibraries.compose)
    implementation(com.gtf.snap.ComposeUiLibraries.composeTool)
    implementation(com.gtf.snap.ComposeUiLibraries.composeMaterial)
    implementation(com.gtf.snap.ComposeUiLibraries.composeFoundation)
    implementation(com.gtf.snap.ComposeUiLibraries.composeActivity)
//    implementation(com.gtf.snap.ComposeUiLibraries.composeFramework)

//    implementation(com.gtf.snap.ComposeUiLibraries.composeToolingPreview)
    implementation(com.gtf.snap.ComposeUiLibraries.composeLayout)
    implementation(com.gtf.snap.ComposeUiLibraries.composeRuntime)
    implementation(project(":ui"))


}