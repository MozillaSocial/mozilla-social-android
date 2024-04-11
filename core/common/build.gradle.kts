plugins {
    id("social.firefly.android.library")
}

android {
    namespace = "social.firefly.core.common"
}

dependencies {
    implementation(libs.jakewharton.timber)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.navigation.compose)
}
