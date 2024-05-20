
buildscript {
    val kotlin_version = "1.9.23"
    val jacoco_version = "0.8.12"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jacoco:org.jacoco.core:$jacoco_version")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:5.1.14")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.android.application").version("8.4.0").apply(false)
    id("com.android.library").version("8.4.0").apply(false)
    id("org.jetbrains.kotlin.android").version("1.9.23").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory.get())
}
