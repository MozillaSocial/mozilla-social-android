plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "org.mozilla.social.feature.media"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:common"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))

    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil)

    implementation(libs.jakewharton.timber)
}
