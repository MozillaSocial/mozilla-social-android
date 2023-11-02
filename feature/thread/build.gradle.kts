plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "org.mozilla.social.feature.thread"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.jakewharton.timber)
    implementation(libs.androidx.navigation.compose)
}