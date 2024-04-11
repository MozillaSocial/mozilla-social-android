package social.firefly

import com.android.build.api.dsl.CommonExtension
import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.SecretsPluginExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Action
import org.gradle.api.Project

fun Project.android(configure: Action<CommonExtension<*, *, *, *, *, *>>) {
    extensions.configure("android", configure)
}

fun Project.detekt(configure: Action<DetektExtension>) {
    extensions.configure("detekt", configure)
}

fun Project.secrets(configure: Action<SecretsPluginExtension>) {
    extensions.configure("secrets", configure)
}
