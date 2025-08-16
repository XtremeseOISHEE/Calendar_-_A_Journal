// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

}

/*repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}*/
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // latest stable
    }
}

