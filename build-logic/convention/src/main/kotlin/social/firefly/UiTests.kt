package social.firefly

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureUiTests(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            add("androidTestImplementation", libs.findLibrary("espresso-core").get())
            add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit").get())
        }
    }
}