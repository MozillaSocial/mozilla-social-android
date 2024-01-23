package org.mozilla.social.core.ui.notifications

import org.koin.dsl.module
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val notificationUiModule = module {
    includes(
        navigationModule,
        mastodonUsecaseModule,
    )

    factory {parametersHolder ->
        NotificationCardDelegate(
            coroutineScope = parametersHolder[0],
            navigateTo = get(),
            acceptFollowRequest = get(),
            denyFollowRequest = get(),
        )
    }
}