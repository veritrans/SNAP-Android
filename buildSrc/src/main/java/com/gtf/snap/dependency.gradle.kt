package com.gtf.snap


object Dependencies {
    const val coreKtxVersion = "1.9.23"
    const val navigationUiKtxVersion = "2.7.7"
    const val navigationFragmentKtxVersion = "2.7.7"
    const val junitVersion = "4.13.1"
    const val androidxJunitVersion = "1.1.2"
    const val androidxArchTestingVersion = "2.2.0"
    const val espressoCoreVersion = "3.5.1"
    const val appCompatVersion = "1.6.1"
    const val materialDesignComponentVersion = "1.2.1"
    const val constraintLayoutVersion = "2.0.4"
    const val daggerVersion = "2.51.1"
    const val mockitoVersion = "2.25.0"
    const val mockitoKotlinVersion = "4.1.0"
    const val mixpanelVersion = "7.5.2"
    const val okHttpVersion = "4.12.0"
    const val curlLoggerInterceptorVersion = "0.1"
    const val chuckVersion = "4.0.0"
    const val retrofitVersion = "2.11.0"
    const val gsonVersion = "2.8.5"
    const val rxAndroid2Version = "2.1.1"
    const val rxJava2Version = "2.2.7"
    const val rxBindingVersion = "3.1.0"
    const val jodaTimeVersion = "2.9.9.4" // TODO: migrate to javatime
    const val jodaTimeTestVersion = "2.9.9"
    const val zxingVersion = "3.4.1"
    const val robolectricVersion = "4.4"
    const val localizationVersion = "1.2.6"
    const val uuidVersion = "3.1.5"
    const val iso8583Version = "1.0.0"
    const val itmsLibVersion = "1.0.1"
    const val androidxTestVersion = "1.1.1"
    const val androidxLibVersion = "1.4.2"
    const val timberVersion = "4.7.1"
    const val roomVersion = "2.2.6"
    const val powerMockVersion = "2.0.0"
    const val mockKVersion = "1.10.6"
    const val asphaltAlohaVersion = "19.7.3"
    const val commonIoVersion = "2.6"
    const val nanohttpd = "2.2.0"
    const val nanolets = "2.3.1"
    const val androidxDatastoreVersion = "1.1.1"
    const val composeRuntimeVersion = "1.6.7"
    const val composeCompilerVersion = "1.5.13"
    const val composeUiVersion = "1.2.1"
    const val composeFoundationVersion = "1.2.1"
    const val composeMaterialVersion = "1.2.1"
    const val coilComposeVersion = "2.6.0"
    const val hamcrestVersion = "2.2"
    const val coreLibraryDesugaringVersion = "1.1.5"
    const val leakCanaryVersion = "2.14"
    const val composeNavComponent = "2.7.7"
}

object CommonLibraries {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin:stdlib:111"
    const val appCompat = "androidx.appcompat:appcompat:${Dependencies.appCompatVersion}"
    const val materialDesignComponent = "com.google.android.material:material:${Dependencies.materialDesignComponentVersion}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayoutVersion}"
    const val uuid = "com.fasterxml.uuid:java-uuid-generator:${Dependencies.uuidVersion}"
    const val mixpanel = "com.mixpanel.android:mixpanel-android:${Dependencies.mixpanelVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${Dependencies.roomVersion}"
    const val roomRxJava2 = "androidx.room:room-rxjava2:${Dependencies.roomVersion}"
    const val timber = "com.jakewharton.timber:timber:${Dependencies.timberVersion}"
    const val zxing = "com.google.zxing:core:${Dependencies.zxingVersion}"
    const val androidxDatastore = "androidx.datastore:datastore-preferences:${Dependencies.androidxDatastoreVersion}"
    const val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:${Dependencies.coreLibraryDesugaringVersion}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanaryVersion}"
}

object AndroidSupportLibraries {
    const val appCompact = "androidx.appcompat:appcompat:${Dependencies.androidxLibVersion}"
    const val design = "com.google.android.material:material:${Dependencies.androidxLibVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Dependencies.androidxLibVersion}"
}


object KotlinExtensionLibraries {
    const val coreKtx = "androidx.core:core-ktx:${Dependencies.coreKtxVersion}"
    const val connavigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Dependencies.navigationUiKtxVersion}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Dependencies.navigationFragmentKtxVersion}"
}

object DaggerLibraries {
    const val dagger = "com.google.dagger:dagger:${Dependencies.daggerVersion}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Dependencies.daggerVersion}"
    const val daggerAndroid = "com.google.dagger:dagger-android:${Dependencies.daggerVersion}"
    const val daggerAndroidCompiler =
        "com.google.dagger:dagger-android-processor:${Dependencies.daggerVersion}"
}

object KaptLibraries {
    const val roomCompiler = "android.arch.persistence.room:compiler:${Dependencies.roomVersion}"
}


object NetworkLibraries {
    const val okHttp = "com.squareup.okhttp3:okhttp:${Dependencies.okHttpVersion}"
    const val okHttpLogging =
        "com.squareup.okhttp3:logging-interceptor:${Dependencies.okHttpVersion}"
    const val okHttpUrlConnection =
        "com.squareup.okhttp3:okhttp-urlconnection:${Dependencies.okHttpVersion}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Dependencies.retrofitVersion}"
    const val retrofitRx = "com.squareup.retrofit2:adapter-rxjava2:${Dependencies.retrofitVersion}"

    const val chuck = "com.github.chuckerteam.chucker:library:${Dependencies.chuckVersion}"
    const val chuckNoOp = "com.github.chuckerteam.chucker:library-no-op:${Dependencies.chuckVersion}"

    const val gson = "com.google.code.gson:gson:${Dependencies.gsonVersion}"
    const val curlLoggerInterceptor =
        "com.github.grapesnberries:curlloggerinterceptor:${Dependencies.curlLoggerInterceptorVersion}"
}


object RxLibraries {
    const val rxAndroid2 = "io.reactivex.rxjava2:rxandroid:${Dependencies.rxAndroid2Version}"
    const val rxJava2 = "io.reactivex.rxjava2:rxjava:${Dependencies.rxJava2Version}"
    const val rxBinding = "com.jakewharton.rxbinding3:rxbinding:${Dependencies.rxBindingVersion}"
}


object JodaTimeLibraries {
    const val jodaTime = "net.danlew:android.joda:${Dependencies.jodaTimeVersion}"
}


object ZxingLibraries {
    const val zxing = "com.journeyapps:zxing-android-embedded:${Dependencies.zxingVersion}"
}


object LocalizationLibraries {
    const val localization = "com.akexorcist:localization:${Dependencies.localizationVersion}"
}

object RoomLibraries {
    const val roomRuntime = "androidx.room:room-runtime:${Dependencies.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Dependencies.roomVersion}"
    const val roomRxJava2 = "androidx.room:room-rxjava2:${Dependencies.roomVersion}"
}

object ApacheLibraries {
    const val commonIo = "commons-io:commons-io:${Dependencies.commonIoVersion}"
}

object ComposeUiLibraries {
    const val compose = "androidx.compose.ui:ui:${Dependencies.composeRuntimeVersion}"
    const val composeFoundation =
        "androidx.compose.foundation:foundation:${Dependencies.composeRuntimeVersion}"
    const val composeTool = "androidx.compose.ui:ui-tooling:${Dependencies.composeRuntimeVersion}"
    const val composeMaterial = "androidx.compose.material:material:${Dependencies.composeRuntimeVersion}"
    const val composeActivity = "androidx.activity:activity-compose:1.4.0"
    const val composeFramework = "androidx.ui:ui-framework:${Dependencies.composeRuntimeVersion}"


    const val composeRuntime = "androidx.compose.runtime:runtime:${Dependencies.composeRuntimeVersion}"
    const val composeLayout =
        "androidx.compose.foundation:foundation-layout:${Dependencies.composeRuntimeVersion}"

    const val composeMaterialIconsExtended =
        "androidx.compose.material:material-icons-extended:${Dependencies.composeRuntimeVersion}"

    const val composeToolingPreview =
        "androidx.compose.ui:ui-tooling-preview:${Dependencies.composeRuntimeVersion}"
    const val composeGoogleFont =
        "androidx.compose.ui:ui-text-google-fonts:${Dependencies.composeRuntimeVersion}"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata:${Dependencies.composeRuntimeVersion}"
    const val composeConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha03"
    const val composeAnimatedDrawable = "androidx.compose.animation:animation-graphics:${Dependencies.composeRuntimeVersion}"
    const val composeAnimation = "androidx.compose.animation:animation:${Dependencies.composeRuntimeVersion}"
    const val composeRxjava = "androidx.compose.runtime:runtime-rxjava2:${Dependencies.composeRuntimeVersion}"
    const val coilCompose = "io.coil-kt:coil-compose:${Dependencies.coilComposeVersion}"
    const val coilComposeGif = "io.coil-kt:coil-gif:${Dependencies.coilComposeVersion}"
    const val composeNavComponent = "androidx.navigation:navigation-compose:${Dependencies.composeNavComponent}"

}

object TestLibraries {
    const val junit = "junit:junit:${Dependencies.junitVersion}"
    const val hamcrest = "org.hamcrest:hamcrest:${Dependencies.hamcrestVersion}"
    const val androidxJunit = "androidx.test.ext:junit:${Dependencies.androidxJunitVersion}"
    const val androidxArchTesting =
        "androidx.arch.core:core-testing:${Dependencies.androidxArchTestingVersion}"
    const val espressoCore =
        "androidx.test.espresso:espresso-core:${Dependencies.espressoCoreVersion}"
    const val robolectric = "org.robolectric:robolectric:${Dependencies.robolectricVersion}"
    const val mockito = "org.mockito:mockito-core:${Dependencies.mockitoVersion}"
    const val mockitoKotlin =
        "org.mockito.kotlin:mockito-kotlin:${Dependencies.mockitoKotlinVersion}"
    const val jodaTime = "joda-time:joda-time:${Dependencies.jodaTimeTestVersion}"
    const val powerMockModule =
        "org.powermock:powermock-module-junit4:${Dependencies.powerMockVersion}"
    const val powerMockApi = "org.powermock:powermock-api-mockito2:${Dependencies.powerMockVersion}"
    const val powerMockCore = "org.powermock:powermock-core:${Dependencies.powerMockVersion}"
    const val powerMockModuleRule =
        "org.powermock:powermock-module-junit4-rule:${Dependencies.powerMockVersion}"
    const val mockK = "io.mockk:mockk:${Dependencies.mockKVersion}"
}

