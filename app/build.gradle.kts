plugins {
    id("org.mozilla.social.android.application")
    id("org.mozilla.social.android.application.compose")
}

android {
    namespace = "org.mozilla.social"

    defaultConfig {
        applicationId = "org.mozilla.social"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile("proguard-rules.pro")
        }
        debug {
            isDefault = true
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:analytics"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:search"))
    implementation(project(":feature:report"))
    implementation(project(":feature:post"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:account"))
    implementation(project(":core:model"))
    implementation(project(":feature:thread"))
    implementation(project(":feature:report"))
    implementation(project(":feature:hashtag"))
    implementation(project(":feature:followers"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:discover"))

    implementation(kotlin("reflect"))

    implementation(libs.google.material)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    implementation(libs.androidx.browser)

    implementation(libs.square.okhttp)

    implementation(libs.coil)
    implementation(libs.coil.gif)
    implementation(libs.coil.video)

    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.koin.test)

    implementation(libs.jakewharton.timber)

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.test.ext.junit)

    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
}