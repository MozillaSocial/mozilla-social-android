package org.mozilla.social.feature.thread

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.ThreadAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val threadModule =
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
            ThreadViewModel(
                analytics = get(),
                getLoggedInUserAccountId = get(),
                getThread = get(),
                mainStatusId = parametersHolder[0],
            )
        }
    }
