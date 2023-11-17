package org.mozilla.social.core.usecase.mozilla

import org.koin.dsl.module
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.database.databaseModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mozilla.mozillaRepositoryModule

val mozillaUsecaseModule =
    module {
        includes(
            mozillaRepositoryModule,
            databaseModule,
            analyticsModule,
            navigationModule,
        )

        single { GetRecommendations(get()) }
    }
