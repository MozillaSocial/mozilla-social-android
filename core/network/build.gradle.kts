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
    implementation(project(":core:model"))

    implementation(libs.retrofit)

    // Core library
    implementation("fr.outadoc.mastodonk:mastodonk-core:+")

    // Paging library, use with androidx.paging v3 on JVM
//    implementation("fr.outadoc.mastodonk:mastodonk-paging:+")

}