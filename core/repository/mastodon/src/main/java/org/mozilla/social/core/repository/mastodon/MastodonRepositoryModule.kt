package org.mozilla.social.core.repository.mastodon

import org.koin.dsl.module
import org.mozilla.social.core.network.mastodon.mastodonNetworkModule

fun mastodonRepositoryModule(isDebug: Boolean) = module {
    single { AuthCredentialObserver(get(), get()) }
    single { StatusRepository(get()) }
    single { AccountRepository(get()) }
    single { TimelineRepository(get()) }
    single { OauthRepository(get()) }
    single { MediaRepository(get()) }
    single { SearchRepository(get()) }
    single { AppRepository(get()) }
    single { InstanceRepository(get()) }
    single { ReportRepository(get()) }
    single {  }
    includes(mastodonNetworkModule(isDebug))
}