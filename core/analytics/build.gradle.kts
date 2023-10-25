plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.secrets")
    alias(libs.plugins.jetbrains.python)
    alias(libs.plugins.glean)
}

android {
    namespace = "org.mozilla.social.core.analytics"
    
    buildTypes {
        release {
            isMinifyEnabled = false
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

    implementation(libs.jakewharton.timber)
}
