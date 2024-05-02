@file:OptIn(ExperimentalPagingApi::class)

package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
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

        singleOf(::AuthCredentialObserver)
        singleOf(::StatusRepository)
        singleOf(::AccountRepository)
        singleOf(::TimelineRepository)
        singleOf(::OauthRepository)
        singleOf(::MediaRepository)
        singleOf(::SearchRepository)
        singleOf(::AppRepository)
        singleOf(::InstanceRepository)
        singleOf(::ReportRepository)
        singleOf(::DatabaseDelegate)
        singleOf(::PollRepository)
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
        singleOf(::TrendingStatusRepository)
        singleOf(::PushRepository)
        singleOf(::FollowedHashTagsRepository)
        singleOf(::BookmarksRepository)
    }
