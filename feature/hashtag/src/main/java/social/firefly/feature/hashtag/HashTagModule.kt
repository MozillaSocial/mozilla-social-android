package social.firefly.feature.hashtag

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.HashtagAnalytics
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val hashTagModule =
    module {
        includes(
            commonModule,
            mastodonUsecaseModule,
            mastodonRepositoryModule,
            dataStoreModule,
            postCardModule,
            navigationModule,
            analyticsModule,
        )

        viewModel { parametersHolder ->
            HashTagViewModel(
                hashTag = parametersHolder[0],
                analytics = get(),
                userAccountId = get(),
                timelineRepository = get(),
                unfollowHashTag = get(),
                followHashTag = get(),
                getHashTag = get(),
            )
        }
    }
