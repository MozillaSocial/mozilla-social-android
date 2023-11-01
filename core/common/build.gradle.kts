plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.common"
}

dependencies {
    implementation(libs.jakewharton.timber)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.navigation.compose)
}