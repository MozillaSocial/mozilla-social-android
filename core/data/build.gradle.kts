plugins {
    id("org.mozilla.social.android.library")
    id("org.mozilla.social.android.library.secrets")

}

android {
    namespace = "org.mozilla.social.core.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.retrofit)

    implementation(libs.koin)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room)
}