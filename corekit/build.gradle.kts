plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("maven-publish")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            ) //TODO: check this working or not
            buildConfigField("String", "SNAP_BASE_URL", "\"https://app.midtrans.com/snap/\"")
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.midtrans.com/v2/\"")
        }

        debug {
            buildConfigField(
                "String",
                "SNAP_BASE_URL",
                "\"https://app.sandbox.midtrans.com/snap/\""
            )
            buildConfigField(
                "String",
                "CORE_API_BASE_URL",
                "\"https://api.sandbox.midtrans.com/v2/\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = ("1.8")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            // 1. configure repositories
            repositories {
                maven {
                    url = uri("https://github.com") //change to repo url
                    // credentials are stored in ~/.gradle/gradle.properties with ~ being the path of the home directory
                    credentials {
                        username = project.properties["maven_user_name"]?.toString()
                            ?: throw IllegalStateException("maven repo user name not found")
                        password = project.properties["maven_password"]?.toString()
                            ?: throw IllegalStateException("maven password not found")
                    }
                }
            }
            // 2. configure publication
            // 3. sign the artifacts
        }
    }
}

dependencies {

    implementation(com.gtf.snap.KotlinExtensionLibraries.coreKtx)
    implementation(com.gtf.snap.AndroidSupportLibraries.appCompact)
    implementation(com.gtf.snap.CommonLibraries.androidxDatastore)
    implementation(com.gtf.snap.JodaTimeLibraries.jodaTime)
    implementation(com.gtf.snap.CommonLibraries.uuid)
    implementation(com.gtf.snap.CommonLibraries.clevertap)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    //dagger
    implementation(com.gtf.snap.DaggerLibraries.dagger)
    kapt(com.gtf.snap.DaggerLibraries.daggerCompiler)
    implementation(com.gtf.snap.DaggerLibraries.daggerAndroid)
    kapt(com.gtf.snap.DaggerLibraries.daggerAndroidCompiler)

    testImplementation(com.gtf.snap.DaggerLibraries.dagger)
    kaptTest(com.gtf.snap.DaggerLibraries.daggerCompiler)
    testImplementation(com.gtf.snap.DaggerLibraries.daggerAndroid)
    kaptTest(com.gtf.snap.DaggerLibraries.daggerAndroidCompiler)

    //rx android
    implementation(com.gtf.snap.RxLibraries.rxAndroid2)
    implementation(com.gtf.snap.RxLibraries.rxJava2)
    implementation(com.gtf.snap.RxLibraries.rxBinding)

    //network rest-client
//    implementation networkLibraries.curlLoggerInterceptor
    implementation(com.gtf.snap.NetworkLibraries.retrofit)
    implementation(com.gtf.snap.NetworkLibraries.retrofitGson)
    implementation(com.gtf.snap.NetworkLibraries.retrofitRx)
    implementation(com.gtf.snap.NetworkLibraries.okHttp)
    implementation(com.gtf.snap.NetworkLibraries.okHttpLogging)
    implementation(com.gtf.snap.NetworkLibraries.okHttpUrlConnection)

    debugImplementation(com.gtf.snap.NetworkLibraries.chuck)
    releaseImplementation(com.gtf.snap.NetworkLibraries.chuckNoOp)


    //testing-tools
    testImplementation(com.gtf.snap.TestLibraries.junit)
    testImplementation(com.gtf.snap.TestLibraries.mockito)
    testImplementation(com.gtf.snap.TestLibraries.mockitoKotlin)
    testImplementation(com.gtf.snap.TestLibraries.robolectric)
    testImplementation(com.gtf.snap.TestLibraries.androidxJunit)
    testImplementation(com.gtf.snap.TestLibraries.androidxArchTesting)
    testImplementation(com.gtf.snap.TestLibraries.jodaTime)
    testImplementation(com.gtf.snap.TestLibraries.powerMockModule)
    testImplementation(com.gtf.snap.TestLibraries.powerMockApi)
    testImplementation(com.gtf.snap.TestLibraries.powerMockCore)
    testImplementation(com.gtf.snap.TestLibraries.powerMockModuleRule)
    testImplementation(com.gtf.snap.TestLibraries.mockK)
}