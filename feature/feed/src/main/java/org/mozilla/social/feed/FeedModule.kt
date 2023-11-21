package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.database.databaseModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.feed.remoteMediators.FederatedTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.HomeTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.LocalTimelineRemoteMediator

val feedModule =
    module {
        includes(
            commonModule,
            mastodonUsecaseModule,
            dataStoreModule,
            mastodonRepositoryModule,
            postCardModule,
            databaseModule,
            navigationModule,
            analyticsModule,
        )

        single {
            HomeTimelineRemoteMediator(
                get(),
            )
        }
        single {
            LocalTimelineRemoteMediator(
                get(),
            )
        }
        single {
            FederatedTimelineRemoteMediator(
                get(),
            )
        }
        viewModel { _ ->
            FeedViewModel(
                analytics = get(),
                homeTimelineRemoteMediator = get(),
                localTimelineRemoteMediator = get(),
                federatedTimelineRemoteMediator = get(),
                getLoggedInUserAccountId = get(),
                timelineRepository = get(),
            )
        }
    }
