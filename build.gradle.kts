// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.0.2" apply false
    id("com.android.dynamic-feature") version "8.2.1" apply false
    id(Plugins.ksp) version Versions.ksp
}

buildscript {
    apply(from = "$rootDir/team-props/git-hooks.gradle.kts")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(ClassPaths.navigation_safeargs_plugin)
        classpath(ClassPaths.kotlin_gradle_plugin)
        classpath(ClassPaths.ksp)
        classpath(ClassPaths.android_gradle_plugin)
        classpath(ClassPaths.dagger_hilt_plugin)
        classpath("com.google.gms:google-services:4.4.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
