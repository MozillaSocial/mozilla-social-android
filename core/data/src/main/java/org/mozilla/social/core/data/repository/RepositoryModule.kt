package org.mozilla.social.core.data.repository

import org.koin.dsl.module

val repositoryModule = module {
    single { MastodonServiceWrapper(get()) }
    single { FeedRepository(get()) }
    single { StatusRepository(get()) }
}