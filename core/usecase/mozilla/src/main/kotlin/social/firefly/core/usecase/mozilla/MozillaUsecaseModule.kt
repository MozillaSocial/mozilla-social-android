package social.firefly.core.usecase.mozilla

import org.koin.dsl.module
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.database.databaseModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mozilla.mozillaRepositoryModule

val mozillaUsecaseModule =
    module {
        includes(
            mozillaRepositoryModule,
            databaseModule,
            analyticsModule,
            navigationModule,
        )
    }
