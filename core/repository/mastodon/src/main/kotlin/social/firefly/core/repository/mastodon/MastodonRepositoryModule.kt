package social.firefly.core.repository.mastodon

import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import social.firefly.core.database.databaseModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.network.mastodon.VERIFICATION_CLIENT
import social.firefly.core.network.mastodon.mastodonNetworkModule

val mastodonRepositoryModule =
    module {
        includes(
            mastodonNetworkModule,
            dataStoreModule,
            databaseModule,
        )

        single { TimelineRepository(get(), get(), get(), get(), get(), get()) }
        factory { parametersHolder ->
            VerificationRepository(
                appApi = get(
                    qualifier = named(VERIFICATION_CLIENT)
                ) {
                    parametersOf(parametersHolder.get<String>())
                },
            )
        }

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
    }
