package org.mozilla.social

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
        }
    }
}