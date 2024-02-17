plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.secrets")
}

android {
    namespace = "social.firefly.core.repository.mozilla"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network:mozilla"))
    implementation(project(":core:model"))
    implementation(libs.koin.core)
}
