package social.firefly.core.ui.notifications

import org.koin.dsl.module
import social.firefly.core.navigation.navigationModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

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