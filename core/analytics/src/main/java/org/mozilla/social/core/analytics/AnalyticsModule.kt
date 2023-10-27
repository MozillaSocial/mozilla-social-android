package org.mozilla.social.core.analytics

import org.koin.dsl.module
import org.mozilla.social.core.analytics.glean.GleanAnalytics

val analyticsModule = module {
    single<Analytics> { GleanAnalytics() }
}