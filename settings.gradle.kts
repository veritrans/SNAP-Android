pluginManagement {
//    apply plugin: 'maven-publish'
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url= uri("https://jitpack.io")}
        maven { url= uri("https://maven.google.com/")}
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url= uri("https://jitpack.io")}
        maven { url= uri("https://maven.google.com/")}
    }
}

rootProject.name = "Snap"
include(":app")
include(":corekit")
include(":ui")
