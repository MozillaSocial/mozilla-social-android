plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.feature.discover"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:ui:accountfollower"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:repository:paging"))


    implementation(libs.androidx.browser)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.coil)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

}
