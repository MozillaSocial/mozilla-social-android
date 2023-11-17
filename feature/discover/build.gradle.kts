plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.feature.discover"
}

dependencies {
    implementation(project(":core:usecase:mozilla"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.browser)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
}
