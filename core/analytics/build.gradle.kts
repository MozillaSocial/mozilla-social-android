plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.secrets")
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
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.jakewharton.timber)
}
