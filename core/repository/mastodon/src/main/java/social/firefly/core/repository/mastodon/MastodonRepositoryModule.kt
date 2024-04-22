@file:OptIn(ExperimentalPagingApi::class)

package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.core.database.databaseModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.network.mastodon.mastodonNetworkModule

val mastodonRepositoryModule =
    module {
        includes(
            mastodonNetworkModule,
            dataStoreModule,
            databaseModule,
        )

        single { AuthCredentialObserver(get(), get()) }
        singleOf(::StatusRepository)
        singleOf(::AccountRepository)
        single { TimelineRepository(get(), get(), get(), get(), get(), get()) }
        single { OauthRepository(get()) }
        single { MediaRepository(get()) }
        singleOf(::SearchRepository)
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
        singleOf(::FavoritesRepository)
        singleOf(::HashtagRepository)
        singleOf(::NotificationsRepository)
        singleOf(::FollowRequestRepository)
        singleOf(::TrendingHashtagRepository)
        factoryOf(::TrendingStatusRepository)
        singleOf(::PushRepository)
    }
