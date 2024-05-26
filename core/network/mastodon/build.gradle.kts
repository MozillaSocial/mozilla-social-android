plugins {
    id("social.firefly.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.core.network.mastodon"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.logging)

    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.json)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)
}
