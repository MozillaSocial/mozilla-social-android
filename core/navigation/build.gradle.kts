plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.navigation"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
}
