plugins {
    id("social.firefly.android.library")
}

android {
    namespace = "social.firefly.core.usecase.mastodon"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui:htmlcontent"))
    implementation(project(":core:accounts"))

    implementation(libs.androidx.browser)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.square.okhttp)
}
