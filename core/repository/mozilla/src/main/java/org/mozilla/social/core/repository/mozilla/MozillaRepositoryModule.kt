package org.mozilla.social.core.repository.mozilla

import org.koin.dsl.module
import org.mozilla.social.core.network.mozilla.mozillaNetworkModule

val mozillaRepositoryModule = module {
    includes(
        mozillaNetworkModule,
    )

    single { RecommendationRepository(get()) }
}