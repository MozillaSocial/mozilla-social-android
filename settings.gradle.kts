pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.mozilla.org/maven2") }
        gradlePluginPortal()
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
rootProject.name = "Firefly"
include(":app")
include(":core:analytics")
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:model")
include(":core:navigation")
include(":core:network:mastodon")
include(":core:network:mozilla")
include(":core:repository:mastodon")
include(":core:repository:mozilla")
include(":core:repository:paging")
include(":core:test")
include(":core:ui:accountfollower")
include(":core:ui:common")
include(":core:ui:htmlcontent")
include(":core:ui:poll")
include(":core:ui:postcard")
include(":core:usecase:mastodon")
include(":core:usecase:mozilla")
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
include(":feature:favorites")
include(":core:workmanager")
include(":feature:notifications")
include(":core:ui:notifications")
include(":feature:media")
