package org.mozilla.social.feature.thread

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val threadModule = module {
    viewModel { parametersHolder -> ThreadViewModel(
        analytics = get(),
        statusRepository = get(),
        accountRepository = get(),
        log = get(),
        accountIdFlow = get(),
        getThreadUseCase = get(),
        openLink = get(),
        navigateTo = get(),
        mainStatusId = parametersHolder[0],
    ) }
}