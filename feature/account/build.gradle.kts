plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.feature.account"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))

    implementation(libs.coil)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.room)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.jakewharton.timber)

    implementation(libs.kotlinx.datetime)
}