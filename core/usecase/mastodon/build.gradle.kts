plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.usecase.mastodon"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))

    implementation(libs.jakewharton.timber)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.room)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.browser)
}