plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
    id("org.mozilla.social.android.library.secrets")
}

android {
    namespace = "org.mozilla.social.feature.auth"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))
    implementation(project(":core:ui:common"))

    implementation(libs.androidx.browser)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.square.okhttp)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.jakewharton.timber)
}
