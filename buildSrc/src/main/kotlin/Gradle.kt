@Suppress("unused")
object Versions {
    // Build tools
    const val ksp = "1.9.22-1.0.17"
    const val kotlin = "1.7.20"
    const val android_gradle_plugin = "4.1.1"

    // UI
    const val appcompat = "1.6.1"
    const val fragment = "1.2.0-rc02"
    const val glide = "4.16.0"
    const val circle_image = "3.1.0"
    const val shimmer = "0.5.0"
    const val lottie = "5.2.0"
    const val toasty = "1.5.2"
    const val swiperefreshlayout = "1.1.0"
    const val viewpager2 = "1.0.0"
    const val constraint_layout = "2.1.4"
    const val material = "1.11.0"
    const val flexBoxLayout = "3.0.0"
    const val viewPager2 = "1.0.0"

    // Component
    const val lifecycle = "2.2.0-alpha03"
    const val navigation = "2.7.6"

    // Permission
    const val dexter = "6.0.2"

    // Data
    const val pagingVersion = "3.3.0"
    const val dataStorePreferenceVersion = "1.1.1"
    const val dataStoreProto = "1.1.1"
    const val preferences = "1.2.1"
    const val room = "2.6.1"
    const val room_coroutines = "2.1.0-alpha04"

    // Utils
    const val coroutines = "1.7.3"
    const val security_crypto = "1.0.0-alpha02"
    const val timber = "5.0.1"
    const val okhttpLoggingVersion = "5.0.0-alpha.14"

    // Dependency Injection
    const val hilt = "2.50"

    // Testing
    const val arch_core = "2.1.0"
    const val androidx_test = "1.2.0"
    const val androidx_test_ext = "1.1.1"
    const val core_ktx = "1.12.0"
    const val espresso = "3.2.0"
    const val junit = "4.12"

    const val mockkVersion = "1.13.11"
    const val mockitoKotlinVersion = "2.2.0"
    const val mockitoCoreVersion = "5.12.0"
    const val mockitoInlineVersion = "5.2.0"

    // Others
    const val multidexVersion = "2.0.1"

    const val qr_code = "1.9.13"

    // Team props
    const val ktlint = "0.51.0-FINAL"
    const val detekt = "1.22.0-RC2"

    // Network
    const val retrofitConverterGson = "2.9.0"
    const val okHttp = "4.12.0"
    const val retrofit = "2.9.0"

    const val leak_canary = "2.9.1"

    const val moshi = "1.15.1"
    const val gson = "2.11.0"

    const val support = "1.7.1"
    const val work = "2.9.0"
    const val koin = "2.2.0"
    const val anko = "0.10.8"
}

@Suppress("unused")
object ClassPaths {
    const val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigation_safeargs_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val dagger_hilt_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val ksp = "com.google.devtools.ksp:symbol-processing-gradle-plugin:${Versions.ksp}"
}

@Suppress("unused")
object Plugins {
    const val androidApp = "com.android.application"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "android"
    const val kotlinParcelize = "kotlin-parcelize"
    const val dagger_hilt = "dagger.hilt.android.plugin"
    const val kotlinApt = "kapt"
    const val javalib = "java-library"
    const val androidLib = "com.android.library"
    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val ksp = "com.google.devtools.ksp"
}

@Suppress("unused")
object Deps {
    const val support_annotations = "androidx.annotation:annotation:${Versions.support}"
    const val support_app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val support_recyclerview = "androidx.recyclerview:recyclerview:${Versions.support}"
    const val support_cardview = "androidx.cardview:cardview:${Versions.support}"
    const val support_constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

    const val support_design = "com.google.android.material:material:${Versions.material}"
    const val support_core_utils = "androidx.legacy:legacy-support-core-utils:${Versions.support}"
    const val support_core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val support_fragment_runtime = "androidx.fragment:fragment:${Versions.fragment}"
    const val support_fragment_runtime_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val support_fragment_testing = "androidx.fragment:fragment-testing:${Versions.fragment}"

    const val security_crypto = "androidx.security:security-crypto:${Versions.security_crypto}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_testing = "androidx.room:room-testing:${Versions.room}"
    const val room_coroutines = "androidx.room:room-coroutines:${Versions.room_coroutines}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
    const val lifecycle_java8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycle_extension = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    const val arch_core_runtime = "androidx.arch.core:core-runtime:${Versions.arch_core}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.arch_core}"

    const val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofit_mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    const val okhttp_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"

    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"

    const val atsl_core = "androidx.test:core:${Versions.androidx_test}"
    const val atsl_ext_junit = "androidx.test.ext:junit:${Versions.androidx_test_ext}"
    const val atsl_runner = "androidx.test:runner:${Versions.androidx_test}"
    const val atsl_rules = "androidx.test:rules:${Versions.androidx_test}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlin_allopen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"

    const val glide_runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val junit = "junit:junit:${Versions.junit}"

    const val work_runtime = "androidx.work:work-runtime:${Versions.work}"
    const val work_testing = "androidx.work:work-testing:${Versions.work}"
    const val work_firebase = "androidx.work:work-firebase:${Versions.work}"
    const val work_runtime_ktx = "androidx.work:work-runtime-ktx:${Versions.work}"

    const val navigation_runtime = "androidx.navigation:navigation-runtime:${Versions.navigation}"
    const val navigation_runtime_ktx = "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    const val navigation_fragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigation_safe_args_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"

    const val koin_android = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koin_viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koin_ext = "org.koin:koin-androidx-ext:${Versions.koin}"
    const val koin_work = "org.koin:koin-androidx-workmanager:${Versions.koin}"

    const val firebase_crashlytics_ktx = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebase_analytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebase_database = "com.google.firebase:firebase-database-ktx"
    const val dexter = "com.karumi:dexter:${Versions.dexter}"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"
    const val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"

    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val qr_code = "me.dm7.barcodescanner:zbar:${Versions.qr_code}"

    const val anko_core = "org.jetbrains.anko:anko:${Versions.anko}"
    const val anko_design = "org.jetbrains.anko:anko-design:${Versions.anko}"

    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val circle_image = "de.hdodenhof:circleimageview:${Versions.circle_image}"
    const val preference = "androidx.preference:preference-ktx:${Versions.preferences}"
    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"

    const val dagger_hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val dagger_hilt_compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    const val dataStorePreference = "androidx.datastore:datastore-preferences:${Versions.dataStorePreferenceVersion}"
    const val dataStoreProto = "androidx.datastore:datastore:${Versions.dataStoreProto}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val toasty = "com.github.GrenderG:Toasty:${Versions.toasty}"
}

object Configuration {
    const val applicationId = "com.example.mvvm"

    const val versionName = "1.0.0"
    const val versionCode = 1

    const val compile_sdk_version = 34
    const val min_sdk_version = 23
    const val target_sdk_version = 34
}
