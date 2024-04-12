plugins {
    id("social.firefly.android.library")
}

android {
    namespace = "social.firefly.core.repository.paging"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    api(project(":core:repository:common"))
    implementation(project(":core:database"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    implementation(libs.androidx.paging.runtime)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)
}
