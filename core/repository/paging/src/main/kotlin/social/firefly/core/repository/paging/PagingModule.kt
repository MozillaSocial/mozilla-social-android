package social.firefly.core.repository.paging

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagers.status.AccountTimelinePager
import social.firefly.core.repository.paging.pagers.accounts.BlocksPager
import social.firefly.core.repository.paging.pagers.accounts.FollowersPager
import social.firefly.core.repository.paging.pagers.accounts.FollowingsPager
import social.firefly.core.repository.paging.pagers.status.BookmarksPager
import social.firefly.core.repository.paging.pagers.status.FavoritesPager
import social.firefly.core.repository.paging.pagers.status.FederatedTimelinePager
import social.firefly.core.repository.paging.pagers.hashTags.FollowedHashTagsPager
import social.firefly.core.repository.paging.pagers.accounts.MutesPager
import social.firefly.core.repository.paging.pagers.accounts.SearchAccountsPager
import social.firefly.core.repository.paging.pagers.hashTags.SearchHashTagPager
import social.firefly.core.repository.paging.pagers.hashTags.TrendingHashTagPager
import social.firefly.core.repository.paging.pagers.notifications.AllNotificationsPager
import social.firefly.core.repository.paging.pagers.notifications.FollowNotificationsPager
import social.firefly.core.repository.paging.pagers.notifications.MentionNotificationsPager
import social.firefly.core.repository.paging.pagers.status.HashTagTimelinePager
import social.firefly.core.repository.paging.pagers.status.LocalTimelinePager
import social.firefly.core.repository.paging.pagers.status.SearchStatusesPager
import social.firefly.core.repository.paging.pagers.status.TrendingStatusPager
import social.firefly.core.repository.paging.remotemediators.HomeTimelineRemoteMediator
import social.firefly.core.repository.paging.sources.DomainBlocksPagingSource
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val pagingModule = module {
    includes(
        mastodonRepositoryModule,
        mastodonUsecaseModule,
    )

    factoryOf(::HomeTimelineRemoteMediator)


    factoryOf(::AllNotificationsPager)
    factoryOf(::BlocksPager)
    factoryOf(::BookmarksPager)
    factoryOf(::DomainBlocksPagingSource)
    factoryOf(::FavoritesPager)
    factoryOf(::FederatedTimelinePager)
    factoryOf(::FollowedHashTagsPager)
    factoryOf(::FollowNotificationsPager)
    factoryOf(::LocalTimelinePager)
    factoryOf(::MentionNotificationsPager)
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

    factory { parametersHolder ->
        FollowersPager(
            accountRepository = get(),
            databaseDelegate = get(),
            followersRepository = get(),
            relationshipRepository = get(),
            accountId = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        FollowingsPager(
            accountRepository = get(),
            databaseDelegate = get(),
            followingsRepository = get(),
            relationshipRepository = get(),
            accountId = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        HashTagTimelinePager(
            timelineRepository = get(),
            saveStatusToDatabase = get(),
            databaseDelegate = get(),
            getInReplyToAccountNames = get(),
            hashTag = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        SearchAccountsPager(
            searchRepository = get(),
            databaseDelegate = get(),
            accountRepository = get(),
            relationshipRepository = get(),
            query = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        SearchHashTagPager(
            databaseDelegate = get(),
            searchRepository = get(),
            hashtagRepository = get(),
            query = parametersHolder[0],
        )
    }

    factory { parametersHolder ->
        SearchStatusesPager(
            searchRepository = get(),
            databaseDelegate = get(),
            getInReplyToAccountNames = get(),
            saveStatusToDatabase = get(),
            query = parametersHolder[0],
        )
    }
}
