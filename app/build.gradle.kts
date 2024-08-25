buildscript {
    apply("../ktlint.gradle.kts")
    apply("autodimension.gradle.kts")
}

plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.kotlinAndroid)
    id(Plugins.detekt).version(Versions.detekt)
    id(Plugins.kotlinParcelize)
    id(Plugins.navigationSafeArgs)
    id(Plugins.dagger_hilt)
    kotlin(Plugins.kotlinApt)
    id(Plugins.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = Configuration.applicationId
    compileSdk = Configuration.compile_sdk_version

    defaultConfig {
        applicationId = Configuration.applicationId
        minSdk = Configuration.min_sdk_version
        targetSdk = Configuration.target_sdk_version
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += listOf("appVariant")
    productFlavors {
        create("dev") {
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "QLNCKH Dev")
        }
        create("prd") {
            resValue("string", "app_name", "QLNCKH")
        }
    }

//    signingConfigs {
//        create("release") {
//            storeFile = file("../keystores/tiv-keystore-release")
//            storePassword = gradleLocalProperties(rootDir).getProperty("storePassword")
//            keyAlias = gradleLocalProperties(rootDir).getProperty("keyAlias")
//            keyPassword = gradleLocalProperties(rootDir).getProperty("keyPassword")
//        }
//    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            resValue("string", "app_name", "QLNCKH UTT-Dev")
        }
        getByName("release") {
            isDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
//            signingConfig = signingConfigs.getByName("release")
            setProguardFiles(
                setOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro",
                ),
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    lint {
        disable += setOf("MissingTranslation")
        checkOnly += setOf("Interoperability")
    }
}

tasks {
    check {
        dependsOn("ktlintCheck")
        dependsOn("ktlintFormat")
    }
}

dependencies {
    implementation(project(":data"))

    implementation(Deps.support_core_ktx)
    implementation(Deps.support_app_compat)
    implementation(Deps.support_design)
    implementation(Deps.support_constraintLayout)
    implementation(Deps.support_annotations)

    implementation(Deps.swiperefreshlayout)
    implementation(Deps.glide_runtime)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    ksp(Deps.glide_compiler)
    implementation(Deps.dexter) // Permission

    // Navigation
    implementation(Deps.navigation_fragment)
    implementation(Deps.navigation_ui)
    implementation(Deps.navigation_fragment_ktx)
    implementation(Deps.navigation_ui_ktx)

    // Circle ImageView
    implementation(Deps.circle_image)

    implementation(Deps.viewpager2)
    implementation(Deps.swiperefreshlayout)

    implementation(Deps.dagger_hilt)
    ksp(Deps.dagger_hilt_compiler)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.espresso_core)
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    // Lifecycler
    implementation(Deps.lifecycle_extension)
    implementation(Deps.lifecycle_livedata_ktx)
    ksp(Deps.lifecycle_compiler)

    implementation(Deps.retrofit_gson)
    implementation(Deps.okhttp_logging_interceptor)

    implementation(Deps.timber)

    implementation(Deps.toasty)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database:21.0.0")

    // DataStore
    implementation(Deps.dataStorePreference)

    // Json
    implementation(Deps.okHttp)
    implementation(Deps.retrofit_runtime) {
        exclude(module = "okhttp")
    }

    // File Picker
    // file picker
    implementation("io.github.chochanaresh:filepicker:0.1.9")
    implementation("com.github.mahdiasd:BottomDialogFilePicker:3.0.4")

    //Dialog Selection
}
