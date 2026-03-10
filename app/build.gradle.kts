// TODO ok
plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android) // con AGP 9+ non serve piu

    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)

//    alias(libs.plugins.kotlin.kapt) // <-- problema, integrato automaticamente
    alias(libs.plugins.ksp) // serve per Room e Hilt

    alias(libs.plugins.hilt.android)
}

// old groovy
//def localProperties = new Properties()
//def localPropertiesFile = rootProject.file('local.properties')
//if (localPropertiesFile.exists()) {
//    localPropertiesFile.withReader('UTF-8') { reader ->
//        localProperties.load(reader)
//    }
//}


// TODO new kts - Se usi Kotlin DSL devi definire prima dell'android{}:
//val localProperties = java.util.Properties().apply {
//    load(rootProject.file("local.properties").inputStream())
//}
//fun getApiKey(name: String): String =
//    localProperties.getProperty(name) ?: ""


android {
    namespace = "com.example.muvitracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.dimao.muvitracker" // modificato per release
        minSdk = 26
        targetSdk = 36
        versionCode = 1_02_00
        versionName = "1.2.0"

        // for api keys on 'local.properties' (2/3) TODO kotlin style
//        buildConfigField 'String', "TRAKT_API_KEY", "\"" + localProperties.trakt_api_key + "\""
//        buildConfigField 'String', "TMDB_API_KEY", "\"" + localProperties.tmdb_api_key + "\""
//        buildConfigField 'String', "OMDB_API_KEY", "\"" + localProperties.omdb_api_key + "\""

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            // debuggable = false -> di default
            // disables minification, so the code will not be removed or reduced.
            // only for the release version, so true it's ok
//            minifyEnabled false // old groovy
            isMinifyEnabled = false // new con dsl

            // uses ProGuard/R8 configuration files for optimization and obfuscation.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug { // default settings, di base non specificato
            applicationIdSuffix = ".debug"
//             minifyEnabled false
//            debuggable true // e ridondante, di default true
        }
    }


    lint {
        // eliminate forces checks on build release
        checkReleaseBuilds = false
        abortOnError = false // TODO X aggiunto
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 //  use java 17 for source code compatibility
        targetCompatibility = JavaVersion.VERSION_17 // generate bytecode compatible with java 17
    }

    // Note
//    kotlinOptions {
//        jvmTarget = "17" // use bytecode compatible with Java 17/ non serve, perché è già la versione del plugin Kotlin
//        languageVersion = "1.9" // use kotlin version 1.9 for the project // TODO non serve
//    }


    buildFeatures {
        buildConfig = true // for api keys on 'local.properties' (3/3)
        viewBinding = true
    }

}

// OLD ----------------------------------------
//dependencies {
//
//// 1) implementation: for app => both debug and release versions
//    // core android libraries
//    implementation 'androidx.appcompat:appcompat:1.6.1'
//    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
//    implementation 'androidx.core:core-ktx:1.12.0' // era ktx:1.12.0 (1.10.1 per sdk33)
//    implementation 'com.google.android.material:material:1.12.0'
//    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0' // swipe refresh
//
//    // kotlin libraries
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.9.24"  // base features of kotlin
//    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3'// alternativa a gson
//
//    // retrofit
//    def retrofit_version = "2.10.0"
//    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
//    implementation "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0" //  koltin serialization for retrofit
////    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version" // not used yet, substitute with kotlin.serialization
//    implementation "com.google.code.gson:gson:2.10.1" // gson indipendente per la conversione su shared, sostituito a quello di retrofit
//
//    def okhttp_version = "4.11.0"
//    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"
//
//    // fragment ktx extension: of base 'androidx.fragment', for 'livedata', etc
//    implementation "androidx.fragment:fragment-ktx:1.7.1" // era ktx:1.7.1 (ktx:1.6.1 per sdk 33)
//
//    // glide
//    def glide_version = "4.16.0"
//    implementation "com.github.bumptech.glide:glide:$glide_version" // glide
//    kapt "com.github.bumptech.glide:compiler:$glide_version" // glide use kapt to generate code
//
//    // hilt dagger
//    def hilt_version = "2.51.1"
//    implementation "com.google.dagger:hilt-android:$hilt_version"
//    kapt "com.google.dagger:hilt-compiler:$hilt_version"
//
//    // coroutines: base + android
//    def coroutines_version = "1.8.1"
//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
//
//    // paging3
//    def paging_version = "3.3.1"
//    implementation "androidx.paging:paging-runtime-ktx:$paging_version"
//
//    // viewPager2, tabs already incleded in m3
//    implementation 'androidx.viewpager2:viewpager2:1.0.0'
//
//    // room for local database
//    def room_version = "2.6.1"
//    implementation "androidx.room:room-ktx:$room_version"
////    annotationProcessor "androidx.room:room-compiler:$room_version" // annotationProcessor for java; use kapt
//    kapt "androidx.room:room-compiler:$room_version"// to use kapt
////    ksp "androidx.room:room-compiler:$room_version"  // to use KSP
//
//    // store - caching helper library for coroutines
//    implementation "org.mobilenativefoundation.store:store4:4.0.7"
//
//
//// 2) unit test implementation
//    testImplementation 'junit:junit:4.13.2' // base test library
////    testImplementation "androidx.arch.core:core-testing:2.2.0"   // specific test library for ViewModel and LiveData
////    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1" // test for coroutines
////    testImplementation "androidx.room:room-testing:$room_version" // room test helpers
//
//
//// 3) android test implementation
//    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
//
//    // AI model for translation
//    implementation 'com.google.mlkit:translate:17.0.3'
//
//    implementation "com.google.android.flexbox:flexbox:3.0.0"
//
//}

// OLD ----------------------------------------
dependencies {

    // core android
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.viewpager2)

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
    ksp(libs.glide.compiler)
//    ksp(libs.glideKsp)

    // hilt
    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler)

    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // paging3
    implementation(libs.paging.runtime.ktx)

    // room
    implementation(libs.room.ktx)
//    kapt(libs.room.compiler)
    ksp(libs.room.compiler)

    // store
    implementation(libs.store4)

    // MLKit
    implementation(libs.mlkit.translate)

    // flexbox
    implementation(libs.flexbox)

    // unit tests
    testImplementation(libs.junit)

    // android tests
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}


// enables more precise error handling and better diagnostics when using annotation processing with kapt,
// like annotation for room, dagger etc
//kapt {
//    correctErrorTypes = true // serve solo per Glide
//}
