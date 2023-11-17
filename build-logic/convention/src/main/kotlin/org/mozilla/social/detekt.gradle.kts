package org.mozilla.social

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    toolVersion = libs.findVersion("detekt").get().toString()
    parallel = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    baseline = file("config/detekt/baseline.xml")
}
