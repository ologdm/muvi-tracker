// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.android.library) apply false // AGP 8+: no longer necessary
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt.android) apply false
//    alias(libs.plugins.kotlin.kapt) apply false       // Legacy kapt (AGP 7/8)
//    alias (libs.plugins.legacy.kapt) apply false      // Kapt support for AGP 9+
    alias(libs.plugins.ksp) apply false // KSP (replaces kapt)
}