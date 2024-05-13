plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.core.ui.chooseAccount"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui:common"))

    implementation(libs.coil)
}
