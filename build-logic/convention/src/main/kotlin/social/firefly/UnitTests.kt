package social.firefly

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureUnitTests(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        dependencies {
            add("testImplementation", libs.findLibrary("mockk").get())
            add("testImplementation", libs.findLibrary("kotlin-test").get())
            add("testImplementation", libs.findLibrary("kotlin-test-junit").get())
            add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            add("testImplementation", libs.findLibrary("google-truth").get())
            add("testImplementation", libs.findLibrary("koin-test").get())
            add("testImplementation", project(":core:test"))
        }
    }
}
