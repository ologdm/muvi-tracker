import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

// -----------------------------------------------------------------------------------------------------
// Load API keys from local.properties (1/3)
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream()) // inputStream: UTF-8 by default
    }
}

fun getApiKey(name: String): String =
    localProperties.getProperty(name) ?: ""

// ----------------------------------------------------------------------------------------------------




android {
    namespace = "com.example.core"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        // API keys loaded from local.properties (2/3)
        // Requires: buildFeatures { buildConfig = true } (3/3)
        buildConfigField ("String", "TRAKT_API_KEY", "\"${getApiKey("trakt_api_key")}\"")
        buildConfigField ("String", "TMDB_API_KEY", "\"${getApiKey("tmdb_api_key")}\"")
        buildConfigField ("String", "OMDB_API_KEY", "\"${getApiKey("omdb_api_key")}\"")


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

    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    //
    implementation(libs.kotlinx.serialization.json) // serve per Json
    implementation(libs.gson)
    api(libs.okhttp) // NOTE: api - condivide la libreria con chi ne dipende
    //
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.test.espresso)

}