package social.firefly.feature.discover

import androidx.paging.ExperimentalPagingApi
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val discoverModule =
    module {
        includes(
            commonModule,
            navigationModule,
            analyticsModule,
            mastodonRepositoryModule,
            mastodonUsecaseModule,
            pagingModule,
            postCardModule,
        )

        viewModelOf(::DiscoverViewModel)
    }
