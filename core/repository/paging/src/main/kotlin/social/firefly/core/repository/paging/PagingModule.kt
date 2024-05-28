package social.firefly.core.repository.paging

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagers.AccountTimelinePager
import social.firefly.core.repository.paging.pagers.BlocksPager
import social.firefly.core.repository.paging.pagers.BookmarksPager
import social.firefly.core.repository.paging.pagers.FavoritesPager
import social.firefly.core.repository.paging.pagers.FollowedHashTagsPager
import social.firefly.core.repository.paging.pagers.MutesPager
import social.firefly.core.repository.paging.pagers.TrendingHashTagPager
import social.firefly.core.repository.paging.pagers.TrendingStatusPager
import social.firefly.core.repository.paging.remotemediators.notifications.AllNotificationsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.notifications.FollowNotificationsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.notifications.MentionNotificationsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.FederatedTimelineRemoteMediator
import social.firefly.core.repository.paging.remotemediators.FollowersRemoteMediator
import social.firefly.core.repository.paging.remotemediators.FollowingsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.HashTagTimelineRemoteMediator
import social.firefly.core.repository.paging.remotemediators.HomeTimelineRemoteMediator
import social.firefly.core.repository.paging.remotemediators.LocalTimelineRemoteMediator
import social.firefly.core.repository.paging.remotemediators.SearchAccountsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.SearchStatusesRemoteMediator
import social.firefly.core.repository.paging.remotemediators.SearchedHashTagsRemoteMediator
import social.firefly.core.repository.paging.sources.DomainBlocksPagingSource
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val pagingModule = module {
    includes(
        mastodonRepositoryModule,
        mastodonUsecaseModule,
        dataStoreModule,
    )

    factoryOf(::HomeTimelineRemoteMediator)
    factoryOf(::LocalTimelineRemoteMediator)
    factoryOf(::FederatedTimelineRemoteMediator)
    factoryOf(::AllNotificationsRemoteMediator)
    factoryOf(::MentionNotificationsRemoteMediator)
    factoryOf(::FollowNotificationsRemoteMediator)

    factory {
        FollowersRemoteMediator(
            accountRepository = get(),
            databaseDelegate = get(),
            followersRepository = get(),
            relationshipRepository = get(),
            accountId = it[0],
        )
    }

    factory {
        FollowingsRemoteMediator(
            accountRepository = get(),
            databaseDelegate = get(),
            followingsRepository = get(),
            relationshipRepository = get(),
            accountId = it[0],
        )
    }

    factory { parametersHolder ->
        HashTagTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        SearchAccountsRemoteMediator(
            searchRepository = get(),
            databaseDelegate = get(),
            accountRepository = get(),
            relationshipRepository = get(),
            query = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        SearchStatusesRemoteMediator(
            searchRepository = get(),
            databaseDelegate = get(),
            getInReplyToAccountNames = get(),
            saveStatusToDatabase = get(),
            query = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        SearchedHashTagsRemoteMediator(
            databaseDelegate = get(),
            searchRepository = get(),
            hashtagRepository = get(),
            query = parametersHolder[0],
        )
    }

    factoryOf(::BlocksPager)
    factoryOf(::BookmarksPager)
    factoryOf(::DomainBlocksPagingSource)
    factoryOf(::FavoritesPager)
    factoryOf(::FollowedHashTagsPager)
    factoryOf(::MutesPager)
    factoryOf(::TrendingStatusPager)
    factoryOf(::TrendingHashTagPager)

    factory { parametersHolder ->
        AccountTimelinePager(
            accountRepository = get(),
            saveStatusToDatabase = get(),
            databaseDelegate = get(),
            timelineRepository = get(),
            getInReplyToAccountNames = get(),
            accountId = parametersHolder[0],
            timelineType = parametersHolder[1],
        )
    }
}
