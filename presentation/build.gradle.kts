plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // KSP (Room, Hilt, Glide) – replaces kapt

}

android {
    namespace = "com.example.presentation"
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

    // base TODO check
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material)
    // others android OK TODO cveck
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.flexbox)

    // test base OK
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)


    // others
    //  -------- Image Loading (Glide) ----------------------------------------- OK, anche su data
    implementation(libs.glide)
    // kapt(libs.glide.compiler)
    // NOTE: Glide KSP does not fully support generated APIs such as GlideApp, GlideRequests, and GlideOptions
    ksp(libs.glide.ksp) // replaces kapt


    //  -------- Pagination ---------------------------------------------------- OK
    implementation(libs.paging.runtime.ktx) // also on data module


    //  -------- Dependency Injection (Hilt) ----------------------------------- TODO move to core
    implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler) // replaces kapt


}