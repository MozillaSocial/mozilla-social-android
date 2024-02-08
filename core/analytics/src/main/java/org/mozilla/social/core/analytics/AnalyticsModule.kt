package org.mozilla.social.core.analytics

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.core.analytics.glean.GleanAnalytics
import org.mozilla.social.core.datastore.dataStoreModule

val analyticsModule =
    module {
        includes(
            dataStoreModule,
        )
        single<Analytics> { GleanAnalytics(get()) }
        singleOf(::AppAnalytics)
    }
