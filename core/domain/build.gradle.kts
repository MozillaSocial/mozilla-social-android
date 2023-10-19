plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))

    implementation(libs.jakewharton.timber)
    implementation(libs.koin)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.room)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.browser)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}