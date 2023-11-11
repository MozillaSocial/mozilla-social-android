package org.mozilla.social.core.repository.mozilla

import org.koin.dsl.module
import org.mozilla.social.core.network.mozilla.mozillaNetworkModule

fun mozillaRepositoryModule(isDebug: Boolean) = module {
    single { RecommendationRepository(get()) }
    includes(mozillaNetworkModule(isDebug))
}