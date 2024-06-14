plugins {
    id("social.firefly.android.library")
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.core.accounts"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.koin.android)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room)

    implementation(libs.kotlinx.serialization.json)
}
