plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.compose")
}

android {
    namespace = "org.mozilla.social.core.ui.htmlcontent"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}
