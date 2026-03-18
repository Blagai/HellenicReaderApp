plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    android {
        namespace = "com.example.hellenicreaderapp"
        compileSdk = 36

        androidResources {
            enable = true
        }
    }
    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
    }
    sourceSets {
        androidMain {

        }
    }
}






