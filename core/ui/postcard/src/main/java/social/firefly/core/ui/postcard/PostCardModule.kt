package social.firefly.core.ui.postcard

import org.koin.core.module.dsl.factoryOf
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

        factoryOf(::PostCardDelegate)
    }
