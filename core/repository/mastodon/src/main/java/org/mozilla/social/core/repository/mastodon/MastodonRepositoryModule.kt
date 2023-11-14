package org.mozilla.social.core.repository.mastodon

import org.koin.dsl.module
import org.mozilla.social.core.network.mastodon.mastodonNetworkModule

fun mastodonRepositoryModule(isDebug: Boolean) = module {
    single { AuthCredentialObserver(get(), get()) }
    single { StatusRepository(get(), get(), get()) }
    single { AccountRepository(get(), get()) }
    single { TimelineRepository(get(), get()) }
    single { OauthRepository(get()) }
    single { MediaRepository(get()) }
    single { SearchRepository(get()) }
    single { AppRepository(get()) }
    single { InstanceRepository(get()) }
    single { ReportRepository(get()) }
    single { DatabaseDelegate(get()) }
    single { PollRepository(get()) }
    includes(mastodonNetworkModule(isDebug))
}