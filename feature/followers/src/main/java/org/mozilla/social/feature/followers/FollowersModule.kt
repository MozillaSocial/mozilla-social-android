package org.mozilla.social.feature.followers

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.FollowersAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val followersModule =
    module {
        includes(
            commonModule,
            mastodonUsecaseModule,
            mastodonRepositoryModule,
            navigationModule,
            analyticsModule,
        )

        viewModel { parameters ->
            FollowersViewModel(
                accountId = parameters[0],
                followingsRepository = get(),
                followersRepository = get(),
                navigateTo = get(),
                analytics = get(),
                followAccount = get(),
                unfollowAccount = get(),
                getLoggedInUserAccountId = get(),
            )
        }
    }
