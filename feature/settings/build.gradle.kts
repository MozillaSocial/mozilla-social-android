plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.feature.settings"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:paging"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:htmlcontent"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:workmanager"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.coil)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.jakewharton.timber)
    implementation(libs.about.libraries.core)
    implementation(libs.about.libraries.ui)
    implementation(libs.koin.androidx.workmanager)
}
