plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.usecase.mozilla"
}

dependencies {
    implementation(project(":core:repository:mozilla"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))

    implementation(libs.koin.core)
}
