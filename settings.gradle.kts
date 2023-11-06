pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.mozilla.org/maven2") }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            // Manually resolve Glean plugin ID to Maven coordinates,
            // because the Maven repository is missing plugin marker artifacts.
            // See: https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_resolution_rules
            if (requested.id.id == "org.mozilla.telemetry.glean-gradle-plugin") {
                useModule("org.mozilla.telemetry:glean-gradle-plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.mozilla.org/maven2") }
    }
}
rootProject.name = "Mozilla Social"
include(":app")
include(":core:designsystem")
include(":core:common")
include(":core:network")
include(":core:data")
include(":feature:auth")
include(":core:datastore")
include(":feature:settings")
include(":feature:feed")
include(":core:model")
include(":feature:post")
include(":feature:search")
include(":core:ui:common")
include(":feature:account")
include(":core:domain")
include(":core:database")
include(":feature:thread")
include(":feature:report")
include(":feature:hashtag")
include(":core:analytics")
include(":feature:followers")
include(":core:navigation")
include(":feature:discover")
include(":core:ui:postcard")
include(":core:ui:poll")
