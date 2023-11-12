plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.storage.mastodon"
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

    implementation(libs.androidx.room)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.room.paging)
}