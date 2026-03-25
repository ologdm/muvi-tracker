plugins {
    // 1. base
    alias(libs.plugins.android.library)
    // 2. optional
//    alias(libs.plugins.kotlin.android) // BUILD WARNING: is no longer required for Kotlin support since AGP 9.0.
    //
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
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

//    testOptions {
//        unitTests.isIncludeAndroidResources = true // NOTE: required if you test Room or other Android libraries
//    }

}


dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    //  -------- Concurrency (Coroutines) --------------------------------------
    implementation(libs.coroutines.core) // coroutines core only, no android

    //  -------- Dependency Injection (Hilt) -----------------------------------
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt

    //  -------- Networking (Retrofit) --------------------------------------------------
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization) // only for conversion
    implementation(libs.kotlinx.serialization.json) // @Serializable, Json config ecc
    implementation(libs.okhttp)

    // --------- Database (Room) -----------------------------------------------
    implementation(libs.room.ktx)
    // kapt(libs.room.compiler)
    ksp(libs.room.compiler) // replaces kapt
    implementation(libs.gson) // for room type converter

    // ---------- Store Caching Library ------------------------------------------------------
    // implementation(libs.store4) // old, not compatible with Kotlin 2.3.10 and agp 9
    implementation(libs.store5)

    // ---------- Paging ----------------------------------------------------------
    implementation(libs.paging.runtime.ktx)

    // ---------- Glide ------------------------------------
    implementation(libs.glide)
    ksp(libs.glide.ksp) // replaces kapt

    // ---------- ML Kit -------------------------------------------------------
    implementation(libs.mlkit.translate)


    // --------- TESTS --------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.test.espresso)

}


room {
    schemaDirectory("$projectDir/schemas")  // <- mandatory for KSP multi-module
}




