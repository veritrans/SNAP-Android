plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
}

android {
    compileSdk = 33
    project.property("sdkVersion")?.let {
        version = it
    }

    defaultConfig {
        minSdk = 21
        targetSdk = 33

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
//        isCoreLibraryDesugaringEnabled = true
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation(com.gtf.snap.CommonLibraries.appCompat)
    testImplementation(com.gtf.snap.TestLibraries.junit)
    testImplementation(com.gtf.snap.TestLibraries.hamcrest)
//    implementation(com.gtf.snap.CommonLibraries.coreLibraryDesugaring)
    testImplementation(com.gtf.snap.TestLibraries.mockitoKotlin)
    testImplementation(com.gtf.snap.TestLibraries.androidxArchTesting)
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
    implementation(com.gtf.snap.ComposeUiLibraries.composeRxjava)
    implementation(com.gtf.snap.ComposeUiLibraries.coilComposeGif)
    implementation(com.gtf.snap.ComposeUiLibraries.coilCompose) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
        exclude(group = "androidx.lifecycle:lifecycle-viewmodel")
        exclude(group = "androidx.lifecycle:lifecycle-viewmodel-ktx")
    }

    //TODO: work around, remove when google fix compose tooling
    debugImplementation("androidx.customview:customview:1.2.0-alpha01")
    debugImplementation("androidx.customview:customview-poolingcontainer:1.0.0-alpha01")
    implementation(project(":corekit"))

    //rx android
    implementation(com.gtf.snap.RxLibraries.rxAndroid2)
    implementation(com.gtf.snap.RxLibraries.rxJava2)
    implementation(com.gtf.snap.RxLibraries.rxBinding)

    implementation(com.gtf.snap.NetworkLibraries.retrofit)

    //dagger
    implementation(com.gtf.snap.DaggerLibraries.dagger)
    kapt(com.gtf.snap.DaggerLibraries.daggerCompiler)
    implementation(com.gtf.snap.DaggerLibraries.daggerAndroid)
    kapt(com.gtf.snap.DaggerLibraries.daggerAndroidCompiler)

    testImplementation(com.gtf.snap.DaggerLibraries.dagger)
    kaptTest(com.gtf.snap.DaggerLibraries.daggerCompiler)
    testImplementation(com.gtf.snap.DaggerLibraries.daggerAndroid)
    kaptTest(com.gtf.snap.DaggerLibraries.daggerAndroidCompiler)

    implementation(com.gtf.snap.CommonLibraries.zxing)

    debugImplementation(com.gtf.snap.CommonLibraries.leakCanary)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.snap"
                artifactId = "uikit-test"
                version = "1.0"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}
