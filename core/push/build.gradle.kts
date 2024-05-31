plugins {
    id("social.firefly.android.library")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "social.firefly.core.push"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(platform(libs.firebase))
    implementation(libs.firebase.messaging)

    implementation(libs.unifiedPush.androidConnector)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
