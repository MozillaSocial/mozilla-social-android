plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.workmanager"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:model"))

    implementation(libs.androidx.work.runtime)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.androidx.workmanager)
}