package org.mozilla.social.core.repository.mastodon

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.core.database.databaseModule
import org.mozilla.social.core.network.mastodon.mastodonNetworkModule

fun mastodonRepositoryModule(isDebug: Boolean) = module {
    single { AuthCredentialObserver(get(), get()) }
    singleOf(::StatusRepository)
    singleOf(::AccountRepository)
    single { TimelineRepository(get(), get()) }
    single { OauthRepository(get()) }
    single { MediaRepository(get()) }
    single { SearchRepository(get()) }
    single { AppRepository(get()) }
    single { InstanceRepository(get()) }
    single { ReportRepository(get()) }
    single { DatabaseDelegate(get()) }
    single { PollRepository(get()) }
    singleOf(::FollowersRepository)
    singleOf(::FollowingsRepository)
    singleOf(::RelationshipRepository)
    singleOf(::MutesRepository)
    singleOf(::BlocksRepository)
    includes(
        mastodonNetworkModule(isDebug),
        databaseModule,
    )
}