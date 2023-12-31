plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.feature.followers"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:accountfollower"))
    implementation(project(":core:repository:paging"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:common"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))

    implementation(libs.coil)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.jakewharton.timber)
}
