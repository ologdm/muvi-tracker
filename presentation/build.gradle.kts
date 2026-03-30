plugins {
// NOTE: The 'com.android.application' plugin must be applied only to the main app module.
// All other modules (presentation, domain, data, core) must use com.android.library.
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt
    alias(libs.plugins.kotlin.parcelize) // only plugin, no library
    alias(libs.plugins.kotlin.compose)
}


android {
    namespace = "com.example.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro") // NOTE: optional for library modules
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
        viewBinding = true
        compose = true
    }

}

dependencies {

    implementation(project(":domain"))
    implementation(project(":core"))

    // -------- Android base ----------------------------------------------
    implementation(libs.androidx.appcompat) // AppCompatActivity, themes
    implementation(libs.androidx.core.ktx) // Kotlin extensions for android
    api(libs.androidx.material) // Material Components (BottomNavigationView, etc.) // NOTE: api for app module
    implementation(libs.androidx.constraintlayout)
    // -------- Android others ----------------------------------------------
    implementation(libs.androidx.fragment.ktx) // Fragment / FragmentManager
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.flexbox) // FlexboxLayout

    // --------- Dependency Injection (Hilt) -----------------------------------
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // replaces kapt


    //  -------- Image Loading (Glide) -----------------------------------------
    implementation(libs.glide)
    ksp(libs.glide.ksp) // NOTE: Glide KSP does not fully support generated APIs such as GlideApp, GlideRequests, and GlideOptions


    //  -------- Pagination ----------------------------------------------------
    implementation(libs.paging.runtime.ktx) // also in data module


    //  -------- Concurrency (Coroutines) --------------------------------------
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android) // needed for lifecycleScope/viewModelScope


    // test - serve su presentation OK
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.test.espresso)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3) // se vuoi UI moderna
    implementation(libs.androidx.activity.compose)   // utile ma non obbligatoria nei Fragment


    // CORE COMPOSE -----------------------------------------------------------------------------------------
    implementation(platform(libs.androidx.compose.bom)) // versionamento automatico ui, material3, foundation
    implementation(libs.androidx.compose.ui) // cuore compose
    implementation(libs.androidx.compose.ui.graphics) // (colori, gradienti, shape, ImageBitmap)
    implementation(libs.androidx.compose.material3) // (Button, TextField, Card, TopAppBar, BottomBar, Dialog, themes Material You)
    //
    implementation(libs.androidx.activity.compose) // collega compose ad activity fragment; serve se usi Compose in Activity principale, nei fragment consigliata
    implementation(libs.androidx.lifecycle.runtime.ktx) // (lifecycleScope, repeatOnLifecycle, collectAsState()) - supporto Kotlin Flow, gestione lifecycle-aware
    //
    implementation(libs.androidx.compose.ui.tooling.preview)



}