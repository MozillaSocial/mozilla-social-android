plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
    id("org.mozilla.social.android.library.secrets")
}

android {
    namespace = "org.mozilla.social.feature.auth"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.browser)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.square.okhttp)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
}