package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.domain.domainModule

val feedModule = module {
    viewModel { parametersHolder -> FeedViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        parametersHolder[0],
        parametersHolder[1],
    ) }
    includes(domainModule)
}