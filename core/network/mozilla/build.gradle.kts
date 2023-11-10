plugins {
    id("org.mozilla.social.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "org.mozilla.social.core.network.mozilla"
}

dependencies {
    implementation(project(":core:common"))

    api(libs.retrofit)

    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.logging)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)
}