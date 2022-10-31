
buildscript {
    val kotlin_version = "1.7.0"
    val jacoco_version = "0.8.4"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jacoco:org.jacoco.core:$jacoco_version")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.18.2")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.android.application").version("7.1.2").apply(false)
    id("com.android.library").version("7.1.2").apply(false)
    id("org.jetbrains.kotlin.android").version("1.5.30").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
