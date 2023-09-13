package org.mozilla.social.feature.thread

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val threadModule = module {
    viewModel { parametersHolder -> ThreadViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        parametersHolder[0],
        parametersHolder[1],
    ) }
}