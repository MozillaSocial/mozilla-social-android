plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.core.ui.postcard"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:mozilla"))
    implementation(project(":core:navigation"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui:common"))
    api(project(":core:ui:htmlcontent"))
    api(project(":core:ui:poll"))

    implementation(libs.androidx.paging.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil)
    implementation(libs.jakewharton.timber)
}