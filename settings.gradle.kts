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
include(":core:analytics")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:navigation")
include(":core:network:mastodon")
include(":core:test")
include(":core:ui:common")
include(":core:ui:htmlcontent")
include(":core:ui:poll")
include(":core:ui:postcard")
include(":feature:auth")
include(":feature:settings")
include(":feature:feed")
include(":feature:post")
include(":feature:search")
include(":feature:account")
include(":feature:thread")
include(":feature:report")
include(":feature:hashtag")
include(":feature:followers")
include(":feature:discover")
include(":core:network:mozilla")
