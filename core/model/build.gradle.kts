plugins {
    id("social.firefly.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.core.model"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.serialization.json)
}
