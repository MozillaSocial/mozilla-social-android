plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.core.ui.common"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:mozilla"))
    implementation(project(":core:navigation"))

    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.kotlinx.datetime)

    implementation(libs.coil)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    implementation(libs.jakewharton.timber)

    implementation(libs.androidx.browser)
}