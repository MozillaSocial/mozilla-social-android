plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.navigation"
}

dependencies {
    implementation(project(":core:ui"))

    implementation(libs.koin)
}