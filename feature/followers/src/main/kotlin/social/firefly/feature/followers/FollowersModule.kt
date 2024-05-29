package social.firefly.feature.followers

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val followersModule =
    module {
        includes(
            commonModule,
            mastodonUsecaseModule,
            mastodonRepositoryModule,
            navigationModule,
            analyticsModule,
            pagingModule,
        )

        viewModel { parameters ->
            FollowersViewModel(
                accountId = parameters[0],
                navigateTo = get(),
                analytics = get(),
                followAccount = get(),
                unfollowAccount = get(),
                getLoggedInUserAccountId = get(),
            )
        }
    }
