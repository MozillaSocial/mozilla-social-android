package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.core.analytics.FeedAnalytics
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.paging.FederatedTimelineRemoteMediator
import org.mozilla.social.core.repository.paging.HomeTimelineRemoteMediator
import org.mozilla.social.core.repository.paging.LocalTimelineRemoteMediator
import org.mozilla.social.core.repository.paging.pagingModule
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val feedModule = module {
    includes(
        commonModule,
        mastodonUsecaseModule,
        dataStoreModule,
        pagingModule,
        postCardModule,
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
    viewModelOf(::FeedViewModel)
}
