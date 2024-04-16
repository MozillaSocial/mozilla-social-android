plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "social.firefly.feature.feed"
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:repository:paging"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:push"))

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.room)

    implementation(libs.androidx.browser)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jakewharton.timber)
}
