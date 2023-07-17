plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "social.mozilla.core.network"

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
    implementation(libs.retrofit)

    // koin
    implementation(libs.koin)
    testImplementation(libs.koin.junit)
}