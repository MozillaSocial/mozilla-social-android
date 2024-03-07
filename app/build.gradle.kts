import java.io.FileInputStream
import java.util.Properties

plugins {
    id("social.firefly.android.application")
    id("social.firefly.android.application.compose")
    alias(libs.plugins.about.libraries.plugin)
    id("social.firefly.android.application.secrets")
}

val keystorePropertiesFile = rootProject.file("secrets/keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "social.firefly"

    defaultConfig {
        applicationId = "social.firefly"
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String?
            keyPassword = keystoreProperties["keyPassword"] as String?
            storeFile = (keystoreProperties["storeFile"] as String?)?.let { file(it) }
            storePassword = keystoreProperties["storePassword"] as String?
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile("proguard-rules.pro")
            matchingFallbacks += "release"
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDefault = true
            applicationIdSuffix = ".debug"
        }
        create("nightly") {
            initWith(getByName("release"))
            applicationIdSuffix = ".nightly"
        }

        create("unsignedRelease") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:repository:mozilla"))
    implementation(project(":core:usecase:mastodon"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:postcard"))
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
    implementation(project(":feature:favorites"))
    implementation(project(":core:workmanager"))
    implementation(project(":feature:notifications"))
    implementation(project(":feature:media"))
    implementation(project(":core:push"))

    implementation(kotlin("reflect"))

    implementation(libs.google.material)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.androidx.workmanager)

    implementation(libs.androidx.browser)

    implementation(libs.square.okhttp)

    implementation(libs.coil)
    implementation(libs.coil.gif)
    implementation(libs.coil.video)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.jakewharton.timber)

    testImplementation(libs.koin.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.test.ext.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
