import java.util.Properties

plugins {
    alias(libs.plugins.android.application) // serve solo qua
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // NOTE: KSP (Room, Hilt, Glide) – replaces kapt
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
        sourceCompatibility = JavaVersion.VERSION_17 //  NOTE: use java 17 for source code compatibility
        targetCompatibility = JavaVersion.VERSION_17 // NOTE: generate bytecode compatible with java 17
    }


    // TODO eliminare, solo presentartion
    buildFeatures {
        viewBinding = true
    }

}


dependencies {
    // --- Moduli core ---
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(project(":core"))


    // --- Android Core ---
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx) // Kotlin Extensions in AndroidX,


    // --- Hilt ---
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt

    // ---------- Testing ------------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.test.espresso)

}


