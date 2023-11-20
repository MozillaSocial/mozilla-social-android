package org.mozilla.social.core.ui.postcard

import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val postCardModule =
    module {
        includes(
            commonModule,
            mastodonRepositoryModule,
            navigationModule,
            mastodonUsecaseModule,
        )

        factory { parametersHolder ->
            PostCardDelegate(
                coroutineScope = parametersHolder[0],
                baseAnalyticsIdentifier = parametersHolder[1],
                navigateTo = get(),
                openLink = get(),
                blockAccount = get(),
                muteAccount = get(),
                voteOnPoll = get(),
                boostStatus = get(),
                undoBoostStatus = get(),
                favoriteStatus = get(),
                undoFavoriteStatus = get(),
                deleteStatus = get(),
                analytics = get(),
            )
        }
    }