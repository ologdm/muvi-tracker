import kotlinx.coroutines.flow.Flow

plugins {
    // base
    alias(libs.plugins.android.library) // serve per usare moduli
    // others
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.domain"
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
    // others
    implementation(libs.coroutines.core) // NOTE: per flow return
    implementation(libs.kotlinx.serialization.json)
    // NOTE: parcelable doesn't have library
    implementation(libs.paging.common.ktx) // OK, serve per separazione clean logica paging

}