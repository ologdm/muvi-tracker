import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization) // TODO eliminare
    // alias(libs.plugins.kotlin.kapt)     // Old kapt plugin used with AGP 7/8
    // alias(libs.plugins.legacy.kapt)     // Replacement for kapt when using AGP 9+
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt
    alias(libs.plugins.hilt.android)
}


android {
    namespace = "com.example.muvitracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.dimao.muvitracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 1_02_00
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {

        debug {
            applicationIdSuffix = ".debug"
        }


        release {
            isMinifyEnabled = false // TODO: test with true,
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    lint {
//        checkReleaseBuilds = false // NOTE: checkReleaseBuilds is not required with AGP 8+
        abortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 //  use java 17 for source code compatibility
        targetCompatibility = JavaVersion.VERSION_17 // generate bytecode compatible with java 17
    }


    // TODO eliminare, solo presentartion
    buildFeatures {
        viewBinding = true
    }

}

// ---------------------------------------------------------------------------------------------------

dependencies {
    implementation(project(":data")) // -> va a build gradle data
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":presentation"))


    //  -------- Core Android --------------------------------------------------
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx) // Kotlin Extensions in AndroidX, todo check
    // presentation only
//    implementation(libs.androidx.material) // NO
//    implementation(libs.androidx.swiperefreshlayout) // NO
    implementation(libs.androidx.fragment.ktx) // per fragm manager
//    implementation(libs.androidx.viewpager2)
//    implementation(libs.flexbox)

    //  -------- Kotlin ------------------------------------------------------
    implementation(libs.kotlin.stdlib) // ?? potrebbe non servire, TODO check
    implementation(libs.kotlinx.serialization.json)  //glideTODO check


    //  -------- Image Loading (Glide) ----------------------------------------- TODO presentation  + data
//    implementation(libs.glide)
    // kapt(libs.glide.compiler)
    // NOTE: Glide KSP does not fully support generated APIs such as GlideApp, GlideRequests, and GlideOptions
//    ksp(libs.glide.ksp) // replaces kapt

    //  -------- Dependency Injection (Hilt) ----------------------------------- TODO OK, su core
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt

    //  -------- Concurrency (Coroutines) --------------------------------------
    implementation(libs.coroutines.core) // TODO check
    implementation(libs.coroutines.android)

    //  -------- Pagination ----------------------------------------------------  TODO spostare
//    implementation(libs.paging.runtime.ktx) // also on data module

    // --------- Database (Room) ----------------------------------------------- TODO OK
//    implementation(libs.room.ktx)
//    // kapt(libs.room.compiler)
//    ksp(libs.room.compiler) // replaces kapt
//    implementation(libs.gson) // per room type converter

    // ---------- Caching ------------------------------------------------------ TODO OK
    // store4 - caching library
    // implementation(libs.store4) // old, not compatible with Kotlin 2.3.10 and agp 9
//    implementation(libs.store5)

    // ---------- Testing ------------------------------------------------------ TODO seeve su app?
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)


    // client HTTP per richieste GET, POST, HEAD,
//    implementation(libs.okhttp) // FIXME: provvisorio, deve stare in core, ma non trova su fragment se voglio fare inject con dagger
}


