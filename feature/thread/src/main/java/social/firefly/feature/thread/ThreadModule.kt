package social.firefly.feature.thread

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.ThreadAnalytics
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

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
