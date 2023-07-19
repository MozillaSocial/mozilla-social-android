plugins {
    id("org.mozilla.social.android.library")
    kotlin("plugin.serialization") version "1.8.21"
}

android {
    namespace = "org.mozilla.social.core.network"

    defaultConfig {
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
}

dependencies {
    implementation(project(":core:model"))

    api(libs.retrofit)

    implementation(libs.koin)
    implementation(libs.koin.android)

    // Core library
    implementation(libs.mastodonk)

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}