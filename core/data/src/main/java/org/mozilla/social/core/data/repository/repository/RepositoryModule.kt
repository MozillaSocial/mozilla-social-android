package org.mozilla.social.core.data.repository.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<MastodonServiceWrapper> { MastodonServiceWrapper(get()) }
    single<FeedRepository> { FeedRepository(get()) }
}