plugins {
    id("org.mozilla.social.android.library")
    kotlin("plugin.serialization") version "1.8.21"
}

android {
    namespace = "org.mozilla.social.core.model"

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
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.core)

    implementation(libs.kotlinx.serialization.json)
}