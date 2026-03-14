import java.util.Properties


// NOTE: temporarely with agp 8, because of store4 library compatible with -> kotlin 1.9.10 -> compatible only with agp 8
// TODO: add AGP 9+
//  - remove kapt with agp 9+ (kap nop more needed), use only ksp -> compatible with glide, hilt, room


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.kotlin.kapt) // old kapt, agp 8,7..
//    alias (libs.plugins.legacy.kapt) // new kapt, agp 9+
    alias(libs.plugins.ksp) // for Room, Hilt, Glide
    alias(libs.plugins.hilt.android)
}

// ------------------------------------------------------------------------

// for API keys on 'local.properties' (1/3)
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream()) // input stream - default UTF-8
    }
}

fun getApiKey(name: String): String =
    localProperties.getProperty(name) ?: ""

// ------------------------------------------------------------------------

android {
    namespace = "com.example.muvitracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.dimao.muvitracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 1_02_00
        versionName = "1.2.0"

        // for API keys on 'local.properties' (2/3)
        // -> buildFeatures { buildConfig = true } (3/3)
        buildConfigField ("String", "TRAKT_API_KEY", "\"${getApiKey("trakt_api_key")}\"")
        buildConfigField ("String", "TMDB_API_KEY", "\"${getApiKey("tmdb_api_key")}\"")
        buildConfigField ("String", "OMDB_API_KEY", "\"${getApiKey("omdb_api_key")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }


    // TODO: check configurazione
    lint {
//        checkReleaseBuilds = false // NOTE: con agp 8+ non necessaria
        abortOnError = false // TODO X aggiunto
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 //  use java 17 for source code compatibility
        targetCompatibility = JavaVersion.VERSION_17 // generate bytecode compatible with java 17
    }


    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

}

// ---------------------------------------------------------------------------

dependencies {

    // core android
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.flexbox)

    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization.json)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.gson)
    implementation(libs.okhttp)

    // glide
    implementation(libs.glide)
//    kapt(libs.glide.compiler)
// NOTE: Il processor KSP di Glide non supporta alcune API generate, tipo: GlideApp, GlideRequests, GlideOptions
    ksp(libs.glide.ksp) // TODO: use with agp 9+

    // hilt
    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // TODO: use with agp 9+

    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // paging3
    implementation(libs.paging.runtime.ktx)

    // room
    implementation(libs.room.ktx)
//    kapt(libs.room.compiler)
    ksp(libs.room.compiler) // TODO: use with agp 9+

    // store4 - caching library
//    implementation(libs.store4)
    implementation(libs.store5)

    // unit tests
    testImplementation(libs.junit)

    // android tests
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)

    // MLKit
    implementation(libs.mlkit.translate)
}


