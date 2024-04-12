plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.secrets")
}

android {
    namespace = "social.firefly.core.repository.mastodon"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network:mastodon"))
    implementation(project(":core:repository:common"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.retrofit)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.paging)
}
