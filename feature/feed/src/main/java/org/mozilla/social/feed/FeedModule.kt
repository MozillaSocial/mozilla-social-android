package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.domain.domainModule

val feedModule = module {
    single { parametersHolder ->
        HomeTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            parametersHolder[0],
        )
    }
    viewModel { parametersHolder -> FeedViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        parametersHolder[0],
    ) }
    includes(domainModule)
}