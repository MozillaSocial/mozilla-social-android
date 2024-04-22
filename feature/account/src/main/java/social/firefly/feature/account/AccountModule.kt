package social.firefly.feature.account

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.AccountTimelineRemoteMediator
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule
import social.firefly.feature.account.edit.EditAccountViewModel

val accountModule =
    module {
        includes(
            commonModule,
            dataStoreModule,
            mastodonUsecaseModule,
            mastodonRepositoryModule,
            postCardModule,
            navigationModule,
            analyticsModule,
            postCardModule,
        )

        viewModel { parametersHolder ->
            AccountViewModel(
                analytics = get(),
                getLoggedInUserAccountId = get(),
                getDetailedAccount = get(),
                navigateTo = get(),
                initialAccountId = parametersHolder[0],
                followAccount = get(),
                unfollowAccount = get(),
                blockAccount = get(),
                unblockAccount = get(),
                muteAccount = get(),
                unmuteAccount = get(),
                timelineRepository = get(),
            )
        }

        viewModelOf(::EditAccountViewModel)
    }
