package social.firefly.core.ui.postcard

import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val postCardModule =
    module {
        includes(
            commonModule,
            mastodonRepositoryModule,
            navigationModule,
            mastodonUsecaseModule,
            analyticsModule,
        )

        factory { parametersHolder ->
            PostCardDelegate(
                feedLocation = parametersHolder[0],
                onHideRepliesClickedCallback = parametersHolder.getOrNull() ?: {},
                appScope = get(),
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
                bookmarkStatus = get(),
                undoBookmarkStatus = get(),
                analytics = get(),
                blockDomain = get(),
            )
        }
    }
