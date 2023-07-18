pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://nexus.outadoc.fr/repository/public") }
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
