plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.secrets")
    alias(libs.plugins.jetbrains.python)
    alias(libs.plugins.glean)
}

android {
    namespace = "social.firefly.core.analytics"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))

    implementation(libs.glean)
    implementation(libs.mozilla.components.service.glean)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.jakewharton.timber)

    testImplementation(libs.glean.forUnitTests)
}
