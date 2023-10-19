plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.core.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation("com.google.android.material:material:1.9.0")

    // compose
    implementation(libs.androidx.compose.material.iconsExtended)
}