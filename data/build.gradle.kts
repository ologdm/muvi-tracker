plugins {
    // 1. base
    alias(libs.plugins.android.library)

    // 2. optional
//    alias(libs.plugins.kotlin.android) // BUILD WARNING: is no longer required for Kotlin support since AGP 9.0.
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // 1. base android library
//    implementation(libs.androidx.core.ktx) // Kotlin Extensions in AndroidX - può stare, ma non è obbligatorio nel data
//    implementation(libs.androidx.appcompat) // UI
//    implementation(libs.androidx.material) // // UI
    testImplementation(libs.junit) // ok
    androidTestImplementation(libs.androidx.test.junit) // ok
    androidTestImplementation(libs.androidx.test.espresso) // ok


//    TODO: implementation(project(":domain"))
    //  -------- Kotlin ------------------------------------------------------
//    implementation(libs.kotlinx.serialization.json) // CHECK: serve??

    //  -------- Networking (Retrofit) --------------------------------------------------
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.retrofit.kotlinx.serialization)


    // --------- Database (Room) ----------------------------------------------- OK
    implementation(libs.room.ktx)
    // kapt(libs.room.compiler)
    ksp(libs.room.compiler) // replaces kapt
    implementation(libs.gson) // per room type converter


    //  -------- Dependency Injection (Hilt) ----------------------------------- OK
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt


    //  -------- Concurrency (Coroutines) -------------------------------------- OK
//    implementation(libs.coroutines.android) // non serve
    implementation(libs.coroutines.core)     // Coroutines (solo core)


    // ---------- Caching ------------------------------------------------------ OK
    // store4 - caching library
    // implementation(libs.store4) // old, not compatible with Kotlin 2.3.10 and agp 9
    implementation(libs.store5)


    implementation(project(":domain")) // -> va a build gradle data

    /* NOTE:
        non devono stare in data
        - AppCompat
        - ConstraintLayout
        - Material
        - Fragment
        - ViewPager2
        - UI libraries

        Il modulo data non dovrebbe conoscere nulla di UI.
     */


}