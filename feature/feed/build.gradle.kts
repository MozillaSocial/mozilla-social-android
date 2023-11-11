plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "org.mozilla.social.feature.feed"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.room)

    implementation(libs.androidx.browser)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jakewharton.timber)
}