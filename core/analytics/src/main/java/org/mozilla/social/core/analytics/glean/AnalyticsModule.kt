package org.mozilla.social.core.analytics.glean

import org.koin.dsl.module

val analyticsModule = module {
    single<Analytics> { GleanAnalytics() }
}