// Root build.gradle.kts file

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false

    // Add the following
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
}
