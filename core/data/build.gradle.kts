plugins {
    id("org.mozilla.social.android.library")
}

android {
    namespace = "org.mozilla.social.core.data"

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

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)

    implementation(libs.koin)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
}