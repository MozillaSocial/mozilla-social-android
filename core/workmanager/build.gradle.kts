plugins {
    id("social.firefly.android.library")
}

android {
    namespace = "social.firefly.core.workmanager"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:model"))

    implementation(libs.androidx.work.runtime)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.androidx.workmanager)
}