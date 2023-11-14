package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.feed.remoteMediators.FederatedTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.HomeTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.LocalTimelineRemoteMediator

val feedModule = module {
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
    viewModel { _ -> FeedViewModel(
        analytics = get(),
        homeTimelineRemoteMediator = get(),
        localTimelineRemoteMediator = get(),
        federatedTimelineRemoteMediator = get(),
        getLoggedInUserAccountId = get(),
        socialDatabase = get(),
    ) }
    includes(mastodonUsecaseModule)
}