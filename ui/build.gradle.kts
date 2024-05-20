import java.net.URI

apply(from = "publish-variables.gradle")


plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
    id("signing")
}

//from publish-variables.gradle
//creating constant variable here only works form publish-variables.gradle, not from gradle.properties(global), somehow it's set to null on "publish",
//also make sure it's unique, using a common name as `groupId` causes the value to be `Snap` not `com.midtrans` which causes error 400 on upload.
val sdkGroupId: String by project.properties
val sdkVersion: String by project.properties
val sdkArtifactId: String by project.properties
val sdkEnvSandbox: String by project.properties
val mavenRepo: String by project.properties
val mavenUrl: String by project.properties
val gitUrl: String by project.properties
val libraryNameUiKit: String by project.properties
val libraryDescription: String by project.properties
val licenseName: String by project.properties
val licenseUrl: String by project.properties
val scmConnection: String by project.properties
val scmDeveloperConnection: String by project.properties
val scmUrl: String by project.properties

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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

        debug {
            enableUnitTestCoverage = true
        }
    }

    flavorDimensions.add("env")
    productFlavors {
        create("sandbox") {
            dimension = "env"
            buildConfigField(
                "String",
                "SNAP_BASE_URL",
                "\"https://app.sandbox.midtrans.com/snap/\""
            )
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.sandbox.midtrans.com/\"")
            buildConfigField(
                "String", "MIXPANEL_TOKEN", project.findProperty("mixpanelTokenSandbox").toString()
            )
            buildConfigField("String", "SDK_VERSION", "\"$sdkVersion\"")
            matchingFallbacks.add("sandbox")
            manifestPlaceholders["isByPassNonSsl"] = false
        }
        create("production") {
            dimension = "env"
            buildConfigField("String", "SNAP_BASE_URL", "\"https://app.midtrans.com/snap/\"")
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.midtrans.com/\"")
            buildConfigField(
                "String",
                "MIXPANEL_TOKEN",
                project.findProperty("mixpanelTokenProduction").toString()
            )
            buildConfigField("String", "SDK_VERSION", "\"$sdkVersion\"")
            matchingFallbacks.add("production")
            manifestPlaceholders["isByPassNonSsl"] = false
        }
    }
    setPublishNonDefault(true)

    compileOptions {
        isCoreLibraryDesugaringEnabled = false
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packaging {
        resources {
            excludes += setOf("META-INF/*.kotlin_module")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = com.gtf.snap.Dependencies.composeCompilerVersion
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.midtrans.sdk.uikit"

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation(com.gtf.snap.CommonLibraries.appCompat)
    testImplementation(com.gtf.snap.TestLibraries.junit)
    testImplementation(com.gtf.snap.TestLibraries.hamcrest)
    implementation(com.gtf.snap.CommonLibraries.coreLibraryDesugaring)
    testImplementation(com.gtf.snap.TestLibraries.mockitoKotlin)
    testImplementation(com.gtf.snap.TestLibraries.androidxArchTesting)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

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
    debugImplementation("androidx.customview:customview:1.2.0-alpha02")
    debugImplementation("androidx.customview:customview-poolingcontainer:1.0.0")

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
            register<MavenPublication>("sandbox") {
                groupId = sdkGroupId
                artifactId = sdkArtifactId
                version = "$sdkVersion-SANDBOX"
                pom {
                    name.set(libraryNameUiKit)
                    description.set(libraryDescription)
                    url.set(gitUrl)
                    licenses {
                        license {
                            name.set(licenseName)
                            url.set(licenseUrl)
                        }
                    }
                    developers {
                        developer {
                            id.set(project.findProperty("developerId")?.toString())
                            name.set(project.findProperty("developerName")?.toString())
                            email.set(project.findProperty("developerEmail")?.toString())
                        }
                    }
                    scm {
                        connection.set(scmConnection)
                        developerConnection.set(scmDeveloperConnection)
                        url.set(scmUrl)
                    }
                }
                afterEvaluate {
                    from(components["sandboxRelease"])
                }
            }

            register<MavenPublication>("production") {
                groupId = sdkGroupId
                artifactId = sdkArtifactId
                version = sdkVersion
                pom {
                    name.set(libraryNameUiKit)
                    description.set(libraryDescription)
                    url.set(gitUrl)
                    licenses {
                        license {
                            name.set(licenseName)
                            url.set(licenseUrl)
                        }
                    }
                    developers {
                        developer {
                            id.set(project.findProperty("developerId")?.toString())
                            name.set(project.findProperty("developerName")?.toString())
                            email.set(project.findProperty("developerEmail")?.toString())
                        }
                    }
                    scm {
                        connection.set(scmConnection)
                        developerConnection.set(scmDeveloperConnection)
                        url.set(scmUrl)
                    }
                }
                afterEvaluate {
                    from(components["productionRelease"])
                }
            }
        }

        repositories {
            maven {
                name = mavenRepo
                url = URI(mavenUrl)
                credentials {
                    username = project.findProperty("ossrhUsername")?.toString()
                    password = project.findProperty("ossrhPassword")?.toString()
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

