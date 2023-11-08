@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.mozilla.social.android.library")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "org.mozilla.social.core.datastore"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
}