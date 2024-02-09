package org.mozilla.social.feature.account

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.AccountAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.repository.paging.AccountTimelineRemoteMediator
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.feature.account.edit.EditAccountViewModel

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
        factory { parametersHolder ->
            AccountTimelineRemoteMediator(
                accountId = parametersHolder[0],
                timelineType = parametersHolder[1],
            )
        }
        viewModelOf(::EditAccountViewModel)
    }
