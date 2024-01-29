package org.mozilla.social.core.repository.paging

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.repository.paging.notifications.AllNotificationsRemoteMediator
import org.mozilla.social.core.repository.paging.notifications.MentionNotificationsRemoteMediator
import org.mozilla.social.core.repository.paging.notifications.FollowNotificationsRemoteMediator
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val pagingModule = module {
    includes(
        mastodonRepositoryModule,
        mastodonUsecaseModule,
    )

    singleOf(::RefreshHomeTimeline)
    singleOf(::RefreshFederatedTimeline)
    singleOf(::RefreshLocalTimeline)
    factoryOf(::FavoritesRemoteMediator)
    factoryOf(::BlocksListRemoteMediator)
    factoryOf(::MutesListRemoteMediator)
    factoryOf(::AllNotificationsRemoteMediator)
    factoryOf(::MentionNotificationsRemoteMediator)
    factoryOf(::FollowNotificationsRemoteMediator)

    factory {
        RefreshAccountTimeline(
            accountRepository = get(),
            saveStatusToDatabase = get(),
            databaseDelegate = get(),
            timelineRepository = get(),
            getInReplyToAccountNames = get(),
            accountId = it[0],
            timelineType = it[1],
        )
    }

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
}
