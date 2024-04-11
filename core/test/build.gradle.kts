plugins {
    id("social.firefly.android.library")
}

android {
    namespace = "social.firefly.core.test"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network:mozilla"))
    implementation(project(":core:network:mastodon"))

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
}
