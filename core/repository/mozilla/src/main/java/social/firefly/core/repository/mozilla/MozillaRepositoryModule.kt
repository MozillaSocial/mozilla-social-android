package social.firefly.core.repository.mozilla

import org.koin.dsl.module
import social.firefly.core.network.mozilla.mozillaNetworkModule

val mozillaRepositoryModule =
    module {
        includes(
            mozillaNetworkModule,
        )

        single { RecommendationRepository(get()) }
    }
