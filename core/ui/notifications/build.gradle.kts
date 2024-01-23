plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.core.ui.notifications"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:ui:poll"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:usecase:mastodon"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.coil)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.jakewharton.timber)
}
