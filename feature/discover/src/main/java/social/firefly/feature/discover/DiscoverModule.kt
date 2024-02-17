package social.firefly.feature.discover

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.usecase.mozilla.mozillaUsecaseModule

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
