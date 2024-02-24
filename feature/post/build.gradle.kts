plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.feature.post"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:model"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.coil)
    implementation(libs.jakewharton.timber)
    implementation(libs.kotlinx.datetime)
}
