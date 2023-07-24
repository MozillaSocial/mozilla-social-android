plugins {
    id("org.mozilla.social.android.library")
    alias(libs.plugins.kotlinx.serialization)
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
    implementation(project(":core:common"))
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.serialization.json)
}