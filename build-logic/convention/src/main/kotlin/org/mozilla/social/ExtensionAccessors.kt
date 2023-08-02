package org.mozilla.social

import com.android.build.api.dsl.CommonExtension
import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.SecretsPluginExtension
import org.gradle.api.Action
import org.gradle.api.Project

fun Project.android(configure: Action<CommonExtension<*, *, *, *, *>>) {
    extensions.configure("android", configure)
}

fun Project.secrets(configure: Action<SecretsPluginExtension>) {
    extensions.configure("secrets", configure)
}
