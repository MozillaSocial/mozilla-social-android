package social.firefly.core.repository.mastodon

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.core.accounts.accountsModule
import social.firefly.core.database.databaseModule
import social.firefly.core.network.mastodon.mastodonNetworkModule

val mastodonRepositoryModule =
    module {
        includes(
            mastodonNetworkModule,
            databaseModule,
            accountsModule,
        )

        single { TimelineRepository(get(), get(), get(), get(), get(), get()) }

        singleOf(::VerificationRepository)
        singleOf(::AuthCredentialObserver)
        singleOf(::StatusRepository)
        singleOf(::AccountRepository)
        singleOf(::MediaRepository)
        singleOf(::SearchRepository)
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
        singleOf(::DomainBlocksRepository)
    }
