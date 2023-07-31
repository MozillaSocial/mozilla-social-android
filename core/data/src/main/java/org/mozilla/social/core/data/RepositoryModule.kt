package org.mozilla.social.core.data

import org.koin.dsl.module
import org.mozilla.social.core.data.repository.AuthRepository
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.core.data.repository.SearchRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.network.networkModule

fun repositoryModule(isDebug: Boolean) = module {
    single { AuthTokenObserver(get(), get()) }
    single { FeedRepository(get()) }
    single { StatusRepository(get()) }
    single { AuthRepository(get()) }
    single { MediaRepository(get()) }
    single { SearchRepository(get()) }
    includes(networkModule(isDebug))
}