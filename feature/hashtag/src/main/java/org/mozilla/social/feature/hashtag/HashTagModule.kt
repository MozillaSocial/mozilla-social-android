package org.mozilla.social.feature.hashtag

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

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
