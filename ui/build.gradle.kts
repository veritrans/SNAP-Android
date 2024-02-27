//import com.google.protobuf.gradle.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
//    id("com.google.protobuf")
//    id("com.google.protobuf").version("0.8.12")
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
            buildConfigField("String", "SNAP_BASE_URL", "\"https://app.midtrans.com/snap/\"")
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.midtrans.com/\"")
            buildConfigField("String", "MIXPANEL_TOKEN", "\"84ed63a9507c49b373945b13633b8a0c\"")
            buildConfigField("String", "SDK_VERSION", "${project.property("sdkVersion")}")
        }

        debug {
            buildConfigField("String", "SNAP_BASE_URL", "\"https://app.sandbox.midtrans.com/snap/\"")
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.sandbox.midtrans.com/\"")
            buildConfigField("String", "MIXPANEL_TOKEN", "\"f070570da8b882fda74c77541f0926a0\"")
            buildConfigField("String", "SDK_VERSION", "${project.property("sdkVersion")}")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = false
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
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

//    sourceSets {
//        getByName("main").java.srcDirs("src/main/java")
//    }
}



dependencies {

////    implementation ("com.google.protobuf:protobuf-javalite:3.19.1")
//    implementation("com.google.protobuf:protobuf-java:3.25.2")
////    implementation ("com.google.protobuf:protobuf-javalite:3.25.2")
//
//    implementation ("com.gojek.clickstream:clickstream-android:2.0.0-alpha-1")
//    implementation ("com.gojek.clickstream:clickstream-lifecycle:2.0.0-alpha-1")
//
//    // Optional
//    implementation ("com.gojek.clickstream:clickstream-health-metrics:2.0.0-alpha-1")
    implementation (files("libs/midtrans-clickstream-proto.jar"))
    implementation(files("libs/clickstream-4.0.5-midtrans.aar"))
    implementation("com.google.protobuf:protobuf-java-util:3.11.1")
//    {
//        exclude( group = "com.google.protobuf.Timestamp")
//    }

    implementation("com.gojek.clickstream:clickstream-event-visualiser:2.0.0-alpha-1")
    implementation("com.gojek.clickstream:clickstream-event-visualiser-ui:2.0.0-alpha-1")
    implementation("com.gojek.clickstream:clickstream-event-listener:2.0.0-alpha-1")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation(com.gtf.snap.CommonLibraries.appCompat)
    testImplementation(com.gtf.snap.TestLibraries.junit)
    testImplementation(com.gtf.snap.TestLibraries.hamcrest)
    implementation(com.gtf.snap.CommonLibraries.coreLibraryDesugaring)
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
    implementation(com.gtf.snap.ComposeUiLibraries.composeNavComponent)

    //TODO: work around, remove when google fix compose tooling
    debugImplementation("androidx.customview:customview:1.2.0-alpha01")
    debugImplementation("androidx.customview:customview-poolingcontainer:1.0.0-alpha01")

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

    // Migrate corekit to uikit
    implementation(com.gtf.snap.NetworkLibraries.retrofit)
    implementation(com.gtf.snap.NetworkLibraries.retrofitGson)
    implementation(com.gtf.snap.NetworkLibraries.retrofitRx)
    implementation(com.gtf.snap.NetworkLibraries.okHttp)
    implementation(com.gtf.snap.NetworkLibraries.okHttpLogging)
    implementation(com.gtf.snap.NetworkLibraries.okHttpUrlConnection)

    debugImplementation(com.gtf.snap.NetworkLibraries.chuck)
    releaseImplementation(com.gtf.snap.NetworkLibraries.chuckNoOp)
    implementation(com.gtf.snap.CommonLibraries.mixpanel)

    implementation(com.gtf.snap.CommonLibraries.androidxDatastore)
    implementation(com.gtf.snap.CommonLibraries.uuid)
}
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("debug") {
                groupId = "com.snap"
                artifactId = "uikit-test"
                version = "1.0"

                afterEvaluate {
                    from(components["debug"])
                }
            }
        }
    }
}

//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:3.25.2"
//    }
//    plugins {
//        this.create("javalite") {
//            artifact = "com.google.protobuf:protoc-gen-javalite:3.25.2"
//        }
//    }
//    generateProtoTasks {
//        all().forEach {
//            it.plugins {
//                create("javalite"){}
//            }
//        }
//    }
//}
//protobuf {
//    protobuf.protoc {
//        artifact = "com.google.protobuf:protoc:3.19.1"
//    }
//    protobuf.plugins {
//        create("kotlin"){
//            artifact = "com.google.protobuf:protoc-gen-javalite:3.19.1"
//        }
//    }
//    protobuf.generateProtoTasks {
//        all().forEach {
//            // omitted plugins config
//            it.builtins {
//                this.id("kotlin")
//            }
//        }
//    }
//}


