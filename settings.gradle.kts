pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.mozilla.org/maven2") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        plugins {
            id("org.mozilla.telemetry.glean-gradle-plugin") version "54.0.0"
            id ("com.jetbrains.python.envs") version "0.0.31"
        }
    }
}

buildscript {
    repositories {
        maven {
            url = uri("https://snapshots.maven.mozilla.org/maven2")
        }
        maven {
            url = uri("https://maven.mozilla.org/maven2")
        }
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.mozilla.telemetry:glean-gradle-plugin:54.0.0")
        classpath("gradle.plugin.com.jetbrains.python:gradle-python-envs:0.0.31")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://nexus.outadoc.fr/repository/public") }
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
include(":core:ui")
include(":feature:account")
include(":core:domain")
include(":core:database")
include(":feature:thread")
include(":feature:report")
include(":feature:hashtag")
include(":core:analytics")
include(":feature:followers")
