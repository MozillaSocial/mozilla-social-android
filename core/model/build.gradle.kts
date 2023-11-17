plugins {
    id("org.mozilla.social.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "org.mozilla.social.core.model"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.serialization.json)
}
