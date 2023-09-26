buildscript {
    dependencies {
        classpath("gradle.plugin.com.jetbrains.python:gradle-python-envs:0.0.31")
        classpath("org.mozilla.telemetry:glean-gradle-plugin:54.0.0")
    }
}

plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.secrets")
}

apply(plugin = "com.jetbrains.python.envs")
apply(plugin = "org.mozilla.telemetry.glean-gradle-plugin")

android {
    namespace = "org.mozilla.social.core.analytics"

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
    implementation(libs.glean)

    implementation(libs.koin)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    testImplementation(libs.glean.forUnitTests)
}
