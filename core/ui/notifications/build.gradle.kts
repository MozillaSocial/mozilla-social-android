plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.core.ui.notifications"
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
