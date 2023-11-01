plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.common"
}

dependencies {
    implementation(libs.jakewharton.timber)
    implementation(libs.koin)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.navigation.compose)
}