plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.domain"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.jakewharton.timber)
    implementation(libs.koin)
    implementation(libs.kotlinx.datetime)
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(libs.androidx.paging.runtime)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}