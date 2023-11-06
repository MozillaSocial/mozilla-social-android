package org.mozilla.social.feature.thread

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val threadModule = module {
    viewModel { parametersHolder -> ThreadViewModel(
        accountIdFlow = get(),
        getThreadUseCase = get(),
        mainStatusId = parametersHolder[0],
    ) }
}