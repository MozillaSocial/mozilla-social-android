plugins {
    id("social.firefly.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.core.push"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:accounts"))

    implementation(platform(libs.firebase))
    implementation(libs.firebase.messaging)

    implementation(libs.unifiedPush.androidConnector)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
