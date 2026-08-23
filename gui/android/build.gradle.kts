// Top-level build file. Android Gradle Plugin 9 uses built-in Kotlin.
// Pin KGP explicitly so the built-in compiler and Compose compiler stay in lockstep.
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.10")
    }
}

plugins {
    id("com.android.application") version "9.3.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10" apply false
}
