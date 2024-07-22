buildscript {
    apply("../ktlint.gradle.kts")
}

plugins {
    id(Plugins.androidLib)
    kotlin(Plugins.kotlinAndroid)
    id(Plugins.kotlinParcelize)
    id(Plugins.detekt).version(Versions.detekt)
    id(Plugins.ksp)
}

tasks {
    check {
        dependsOn("ktlintCheck")
        dependsOn("ktlintFormat")
    }
}

android {
    namespace = "com.example.data"
    compileSdk = Configuration.compile_sdk_version

    defaultConfig {
        minSdk = Configuration.min_sdk_version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(
            file("proguard-rules.pro"),
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                file("proguard-rules.pro"),
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Deps.support_core_ktx)
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.atsl_runner)
    androidTestImplementation(Deps.espresso_core)

    // Json
    implementation(Deps.okHttp)
    implementation(Deps.retrofit_runtime) {
        exclude(module = "okhttp")
    }

    // Dagger Hilt
    implementation(Deps.dagger_hilt)
    ksp(Deps.dagger_hilt_compiler)

    implementation(Deps.retrofit_gson)
    implementation(Deps.okhttp_logging_interceptor)

    // Coroutine
    implementation(Deps.coroutines_core)
    implementation(Deps.coroutines_android)
    implementation(Deps.support_core_ktx)
    testImplementation(Deps.coroutines_test)

    // DataStore
    implementation(Deps.dataStorePreference)
}
