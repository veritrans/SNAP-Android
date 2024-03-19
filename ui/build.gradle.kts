import java.net.URI
apply(from = "publish-variables.gradle")


plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
}

//from publish-variables.gradle
//creating constant here only works form publish-variables, not from gradle.properties(global), somehow it set to null on "publish"
val sdkVersion = project.findProperty("sdkVersion")?.toString()
val mavenRepo = project.findProperty("mavenRepo")?.toString()
val mavenUrl = project.findProperty("mavenUrl")?.toString()
val mixpanelTokenSandbox = project.findProperty("mixpanelTokenSandbox")?.toString()
val mixpanelTokenProduction = project.findProperty("mixpanelTokenProduction")?.toString()

android {
    compileSdk = 33

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

        debug {
            isTestCoverageEnabled = true
        }
    }

    flavorDimensions.add("env")
    productFlavors {
        create("sandbox") {
            dimension = "env"
            buildConfigField("String", "SNAP_BASE_URL", "\"https://app.sandbox.midtrans.com/snap/\"")
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.sandbox.midtrans.com/\"")
            buildConfigField("String", "MIXPANEL_TOKEN", "$mixpanelTokenSandbox")
            buildConfigField("String", "SDK_VERSION", "\"$sdkVersion\"")
            matchingFallbacks.add("sandbox")
            manifestPlaceholders["isByPassNonSsl"] = false
        }
        create("production") {
            dimension = "env"
            buildConfigField("String", "SNAP_BASE_URL", "\"https://app.midtrans.com/snap/\"")
            buildConfigField("String", "CORE_API_BASE_URL", "\"https://api.midtrans.com/\"")
            buildConfigField("String", "MIXPANEL_TOKEN", "$mixpanelTokenProduction")
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
            register<MavenPublication>("sandbox") {
                groupId = project.findProperty("groupId")?.toString()
                artifactId = project.findProperty("artifactId")?.toString()
                version = project.findProperty("sdkVersion")?.toString() + "-SANDBOX"
//                artifact("$buildDir/outputs/aar/ui-sandbox-release.aar")

                pom {
                    name.set(project.findProperty("libraryNameUiKit")?.toString())
                    description.set(project.findProperty("libraryDescription")?.toString())
                    url.set(project.findProperty("gitUrl")?.toString())
                    licenses {
                        license {
                            name.set(project.findProperty("licenseName")?.toString())
                            url.set(project.findProperty("licenseUrl")?.toString())
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
                        connection.set(project.findProperty("scmConnection")?.toString())
                        developerConnection.set(project.findProperty("scmDeveloperConnection")?.toString())
                        url.set(project.findProperty("scmUrl")?.toString())
                    }
                }
                afterEvaluate {
                    from(components["sandboxRelease"])
                }
            }

            register<MavenPublication>("production") {
                groupId = project.findProperty("groupId")?.toString()
                artifactId = project.findProperty("artifactId")?.toString()
                version = project.findProperty("sdkVersion")?.toString()
//                artifact("$buildDir/outputs/aar/ui-production-release.aar")

                pom {
                    name.set(project.findProperty("libraryNameUiKit")?.toString())
                    description.set(project.findProperty("libraryDescription")?.toString())
                    url.set(project.findProperty("gitUrl")?.toString())
                    licenses {
                        license {
                            name.set(project.findProperty("licenseName")?.toString())
                            url.set(project.findProperty("licenseUrl")?.toString())
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
                        connection.set(project.findProperty("scmConnection")?.toString())
                        developerConnection.set(project.findProperty("scmDeveloperConnection")?.toString())
                        url.set(project.findProperty("scmUrl")?.toString())
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
                url = URI (mavenUrl)
                credentials {
                    username = project.findProperty("ossrhUsername")?.toString()
                    password = project.findProperty("ossrhPassword")?.toString()
                }
            }
        }
    }
}
