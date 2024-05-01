package social.firefly.core.ui.hashtagcard

import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val hashTagCardModule = module {
    includes(
        commonModule,
        mastodonRepositoryModule,
        navigationModule,
        mastodonUsecaseModule,
    )

    factory { parametersHolder ->
        HashTagCardDelegate(
            navigateTo = get(),
            coroutineScope = parametersHolder[0],
            followHashTag = get(),
            unfollowHashTag = get(),
        )
    }
}