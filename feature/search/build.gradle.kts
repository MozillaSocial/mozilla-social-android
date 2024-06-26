plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.feature.search"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:paging"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:common"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:ui:accountfollower"))
    implementation(project(":core:ui:hashtagcard"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.jakewharton.timber)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}
