plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.network"

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
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))

    implementation(libs.retrofit)

    // koin
    implementation(libs.koin)
    testImplementation(libs.koin.junit)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
}