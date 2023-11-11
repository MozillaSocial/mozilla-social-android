plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "org.mozilla.social.feature.report"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:htmlcontent"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.coil)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.jakewharton.timber)

    implementation(libs.kotlinx.datetime)
}