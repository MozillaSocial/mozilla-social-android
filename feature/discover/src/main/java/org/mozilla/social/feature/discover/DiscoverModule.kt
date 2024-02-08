package org.mozilla.social.feature.discover

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.DiscoverAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.usecase.mozilla.mozillaUsecaseModule

val discoverModule =
    module {
        includes(
            commonModule,
            mozillaUsecaseModule,
            navigationModule,
            analyticsModule,
        )

        viewModelOf(::DiscoverViewModel)
    }
