plugins {
    id("social.firefly.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.core.network.mozilla"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.logging)

    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.json)

    implementation(libs.kotlinx.datetime)

    implementation(libs.koin.core)

    implementation(libs.kotlinx.serialization.json)
}
