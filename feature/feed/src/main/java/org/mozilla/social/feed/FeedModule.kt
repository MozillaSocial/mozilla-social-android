package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.domain.domainModule
import org.mozilla.social.feed.remoteMediators.FederatedTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.HomeTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.LocalTimelineRemoteMediator

val feedModule = module {
    single {
        HomeTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        LocalTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        FederatedTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
        )
    }
    viewModel { parametersHolder -> FeedViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
    ) }
    includes(domainModule)
    includes(domainModule)
}