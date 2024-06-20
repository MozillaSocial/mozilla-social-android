plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "social.firefly.feature.thread"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))
    implementation(project(":core:accounts"))

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.jakewharton.timber)
    implementation(libs.androidx.navigation.compose)
}
