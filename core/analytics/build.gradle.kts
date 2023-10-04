buildscript {
    dependencies {
        classpath(libs.glean.gradlePlugin)
        classpath(libs.jetbrains.python.gradlePlugin)
    }
}

plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.secrets")
}

apply(plugin = libs.plugins.jetbrains.python.get().pluginId)
apply(plugin = libs.plugins.glean.gradle.plugin.get().pluginId)

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
    implementation(libs.mozilla.components.service.glean)

    implementation(libs.koin)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    testImplementation(libs.glean.forUnitTests)
}
