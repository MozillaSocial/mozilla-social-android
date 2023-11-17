plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.secrets")
}

android {
    namespace = "org.mozilla.social.core.repository.mozilla"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network:mozilla"))
    implementation(project(":core:model"))
    implementation(libs.koin.core)
}
