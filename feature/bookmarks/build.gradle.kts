plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.feature.bookmarks"
}

dependencies {
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:database"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:paging"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}
