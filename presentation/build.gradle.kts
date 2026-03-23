plugins {
    alias(libs.plugins.android.application) // con app library non serve?
    //    alias(libs.plugins.android.library)

    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt

    alias(libs.plugins.kotlin.parcelize)

}


android {
    namespace = "com.example.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro") // TODO check
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

    // NOTE: serve per presentation OK
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    // android base -  NOTE: Tutto utile se i tuoi fragment lo usano
    implementation(libs.androidx.appcompat) // AppCompatActivity, themes
    implementation(libs.androidx.core.ktx) // estensioni Kotlin
    implementation(libs.androidx.material) // Material Components (BottomNavigationView, etc.)
    // android others
    implementation(libs.androidx.fragment.ktx) // Fragment / FragmentManager
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.flexbox) // FlexboxLayout

    // test - serve su presentation OK
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.test.espresso)


    // Dependency Injection (Hilt) - ok averlo in presentation
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt


    //  -------- Image Loading (Glide) ----------------------------------------- OK, anche su data
    implementation(libs.glide)
    // kapt(libs.glide.compiler)
    // NOTE: Glide KSP does not fully support generated APIs such as GlideApp, GlideRequests, and GlideOptions
    ksp(libs.glide.ksp) // replaces kapt


    //  -------- Pagination ---------------------------------------------------- OK
    implementation(libs.paging.runtime.ktx) // also on data module


    //  -------- Concurrency (Coroutines) --------------------------------------
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // android base, testing
    // coroutines
    // hilt
    // pagination
    // glide
    // parcelize plugin
    //
    //
    //


}