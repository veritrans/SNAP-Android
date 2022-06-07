package com.gtf.snap


object Dependencies {
    const val coreKtxVersion = "1.3.2"
    const val navigationUiKtxVersion = "2.3.1"
    const val navigationFragmentKtxVersion = "2.3.1"
    const val junitVersion = "4.13.1"
    const val androidxJunitVersion = "1.1.2"
    const val androidxArchTestingVersion = "2.1.0"
    const val espressoCoreVersion = "3.3.0"
    const val appCompatVersion = "1.2.0"
    const val materialDesignComponentVersion = "1.2.1"
    const val constraintLayoutVersion = "2.0.4"
    const val daggerVersion = "2.26"
    const val mockitoVersion = "2.25.0"
    const val mockitoKotlinVersion = "2.0.0"
    const val clevertapVersion = "4.4.0"
    const val okHttpVersion = "3.12.1"
    const val curlLoggerInterceptorVersion = "0.1"
    const val chuckVersion = "3.5.2"
    const val retrofitVersion = "2.5.0"
    const val gsonVersion = "2.8.5"
    const val rxAndroid2Version = "2.1.0"
    const val rxJava2Version = "2.2.7"
    const val rxBindingVersion = "3.0.0-alpha2"
    const val jodaTimeVersion = "2.9.9.4"
    const val jodaTimeTestVersion = "2.9.9"
    const val zxingVersion = "3.6.0"
    const val robolectricVersion = "4.4"
    const val localizationVersion = "1.2.6"
    const val uuidVersion = "3.1.5"
    const val iso8583Version = "1.0.0"
    const val itmsLibVersion = "1.0.1"
    const val androidxTestVersion = "1.1.1"
    const val androidxLibVersion = "1.0.0"
    const val timberVersion = "4.7.1"
    const val roomVersion = "2.2.6"
    const val powerMockVersion = "2.0.0"
    const val mockKVersion = "1.10.6"
    const val asphaltAlohaVersion = "19.7.3"
    const val commonIoVersion = "2.6"
    const val nanohttpd = "2.2.0"
    const val nanolets = "2.3.1"
    const val androidxDatastoreVersion = "1.0.0"
    const val composeVersion = "1.2.0-beta03"
}

object CommonLibraries {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin:stdlib:111"
    const val appCompat = "androidx.appcompat:appcompat:${Dependencies.appCompatVersion}"
    const val materialDesignComponent =
        "com.google.android.material:material:${Dependencies.materialDesignComponentVersion}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayoutVersion}"
    const val uuid = "com.fasterxml.uuid:java-uuid-generator:${Dependencies.uuidVersion}"
    const val clevertap =
        "com.clevertap.android:clevertap-android-sdk:${Dependencies.clevertapVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${Dependencies.roomVersion}"
    const val roomRxJava2 = "androidx.room:room-rxjava2:${Dependencies.roomVersion}"
    const val timber = "com.jakewharton.timber:timber:${Dependencies.timberVersion}"
    const val zxing = "com.journeyapps:zxing-android-embedded:${Dependencies.zxingVersion}"
    const val androidxDatastore = "androidx.datastore:datastore-preferences:${Dependencies.androidxDatastoreVersion}"

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


//    gojekLibraries = [
//        asphalt     = "com.gojek.android:asphalt:${asphaltVersion}",
//        asphaltAloha: "com.gojek.android:asphalt-aloha:${asphaltAlohaVersion}"
//    ]

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
    const val chuckNoOp = "com.github.chuckerteam.chucker:library:${Dependencies.chuckVersion}"

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
//    def composeVersion = "1.0.0-alpha06"
//    ...
    const val compose = "androidx.compose.ui:ui:${Dependencies.composeVersion}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Dependencies.composeVersion}"
    const val composeTool = "androidx.ui:ui-tooling:${Dependencies.composeVersion}"
    const val composeMaterial = "androidx.compose.material:material:${Dependencies.composeVersion}"
    const val composeActivity = "androidx.activity:activity-compose:1.4.0"
    const val composeFramework = "androidx.ui:ui-framework:${Dependencies.composeVersion}"


    const val composeRuntime = "androidx.compose.runtime:runtime:${Dependencies.composeVersion}"
    const val composeLayout = "androidx.compose.foundation:foundation-layout:${Dependencies.composeVersion}"

    const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Dependencies.composeVersion}"

    const val composeToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Dependencies.composeVersion}"

}

object TestLibraries {
    const val junit = "junit:junit:${Dependencies.junitVersion}"
    const val androidxJunit = "androidx.test.ext:junit:${Dependencies.androidxJunitVersion}"
    const val androidxArchTesting =
        "androidx.arch.core:core-testing:${Dependencies.androidxArchTestingVersion}"
    const val espressoCore =
        "androidx.test.espresso:espresso-core:${Dependencies.espressoCoreVersion}"
    const val robolectric = "org.robolectric:robolectric:${Dependencies.robolectricVersion}"
    const val mockito = "org.mockito:mockito-core:${Dependencies.mockitoVersion}"
    const val mockitoKotlin =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependencies.mockitoKotlinVersion}"
    const val jodaTime = "joda-time:joda-time:${Dependencies.jodaTimeTestVersion}"
    const val powerMockModule =
        "org.powermock:powermock-module-junit4:${Dependencies.powerMockVersion}"
    const val powerMockApi = "org.powermock:powermock-api-mockito2:${Dependencies.powerMockVersion}"
    const val powerMockCore = "org.powermock:powermock-core:${Dependencies.powerMockVersion}"
    const val powerMockModuleRule =
        "org.powermock:powermock-module-junit4-rule:${Dependencies.powerMockVersion}"
    const val mockK = "io.mockk:mockk:${Dependencies.mockKVersion}"
}

