plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.feature.settings"
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:paging"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:htmlcontent"))
    implementation(project(":core:usecase:mastodon"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.coil)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.jakewharton.timber)
}
