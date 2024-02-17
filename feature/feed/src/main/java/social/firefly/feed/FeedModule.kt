package social.firefly.feed

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.paging.FederatedTimelineRemoteMediator
import social.firefly.core.repository.paging.HomeTimelineRemoteMediator
import social.firefly.core.repository.paging.LocalTimelineRemoteMediator
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

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
