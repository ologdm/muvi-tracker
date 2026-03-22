plugins {
    // 1. base
    alias(libs.plugins.android.library)
    // 2. optional
//    alias(libs.plugins.kotlin.android) // BUILD WARNING: is no longer required for Kotlin support since AGP 9.0.
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt
    alias(libs.plugins.kotlin.serialization)
    alias (libs.plugins.room)
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
    implementation(project(":domain"))
    implementation(project(":core"))

    // TESTS
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.test.espresso) // ??

    //  -------- Concurrency (Coroutines) -------------------------------------- OK
    implementation(libs.coroutines.core) // coroutines core only OK

    //  -------- Dependency Injection (Hilt) ----------------------------------- OK
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt

    //  -------- Networking (Retrofit) -------------------------------------------------- OK
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization) // only for conversion
    implementation(libs.kotlinx.serialization.json) // @Serializable, Json config ecc
    implementation(libs.okhttp)

    // --------- Database (Room) ----------------------------------------------- OK
    implementation(libs.room.ktx)
    // kapt(libs.room.compiler)
    ksp(libs.room.compiler) // replaces kapt
    implementation(libs.gson) // per room type converter

    // ---------- Caching ------------------------------------------------------ OK
    // store4 - caching library
    // implementation(libs.store4) // old, not compatible with Kotlin 2.3.10 and agp 9
    implementation(libs.store5)

    // ---------- Paging ----------------------------------------------------------
    implementation(libs.paging.runtime.ktx) // TODO spostare su presentation

    // ---------- ML Kit -------------------------------------------------------
    implementation(libs.mlkit.translate)

    // ---------- Glide ------------------------------------
    implementation(libs.glide)
    ksp(libs.glide.ksp) // replaces kapt

}

room {
    schemaDirectory("$projectDir/schemas")  // <- obbligatorio per KSP multi-module
}


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



