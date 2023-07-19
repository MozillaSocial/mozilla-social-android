package org.mozilla.social.core.data.repository

import org.koin.dsl.module
import org.mozilla.social.core.network.networkModule

val repositoryModule = module {
    single { MastodonServiceWrapper(get()) }
    single { FeedRepository(get()) }
    single { StatusRepository(get()) }
    single { AuthRepository(get()) }
    includes(networkModule)
}